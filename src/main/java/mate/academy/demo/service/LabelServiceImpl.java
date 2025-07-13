package mate.academy.demo.service;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.label.CreateLabelRequestDto;
import mate.academy.demo.dto.label.LabelDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.LabelMapper;
import mate.academy.demo.model.Label;
import mate.academy.demo.repository.LabelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;
    private final ProjectService projectService;

    @Override
    public LabelDto save(CreateLabelRequestDto requestDto) {
        for (Long projectId: requestDto.getProjectsId()) {
            if (!projectService.existById(projectId)) {
                throw new EntityNotFoundException("Can't find project with id: " + projectId);
            }
        }
        Label label = labelMapper.toModel(requestDto);

        return labelMapper.toDto(labelRepository.save(label));
    }

    @Override
    public Page<LabelDto> findAllByProjectId(Long projectId, Pageable pageable) {
        return labelRepository.findAllByProjectId(projectId, pageable)
                .map(labelMapper::toDto);
    }

    @Override
    public LabelDto update(CreateLabelRequestDto requestDto, Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find label with id: " + id
                ));

        labelMapper.updateModelFromDto(requestDto, label);

        return labelMapper.toDto(labelRepository.save(label));
    }

    @Override
    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
