package mate.academy.demo.service;

import mate.academy.demo.dto.project.CreateProjectRequestDto;
import mate.academy.demo.dto.project.ProjectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectDto save(CreateProjectRequestDto requestDto);

    ProjectDto findById(Long id);

    Page<ProjectDto> findAll(Pageable pageable);

    ProjectDto update(CreateProjectRequestDto requestDto, Long id);

    void delete(Long id);

    boolean existById(Long id);
}
