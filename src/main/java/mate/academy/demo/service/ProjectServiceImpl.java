package mate.academy.demo.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.project.CreateProjectRequestDto;
import mate.academy.demo.dto.project.ProjectDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.ProjectMapper;
import mate.academy.demo.model.Project;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final AuthenticationService authenticationService;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final VerificationService verificationService;

    @Override
    public ProjectDto save(CreateProjectRequestDto requestDto) {
        Project project = projectMapper.toModel(requestDto);
        User user = new User();
        user.setId(authenticationService.getCurrentUserId());
        project.setUsers(Set.of(user));

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public ProjectDto findById(Long id) {
        verificationService.isCurrentUserRelatedToProject(id);

        return projectMapper.toDto(projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find project with id: " + id
                )));
    }

    @Override
    public Page<ProjectDto> findAll(Pageable pageable) {
        return projectRepository.findAllByUserId(authenticationService.getCurrentUserId(),
                pageable).map(projectMapper::toDto);
    }

    @Override
    public ProjectDto update(CreateProjectRequestDto requestDto, Long id) {
        verificationService.isCurrentUserRelatedToProject(id);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find project with id: " + id
                ));

        projectMapper.updateModelFromDto(requestDto, project);

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long id) {
        verificationService.isCurrentUserRelatedToProject(id);

        projectRepository.deleteById(id);
    }

    @Override
    public boolean existById(Long id) {
        return projectRepository.existsById(id);
    }
}
