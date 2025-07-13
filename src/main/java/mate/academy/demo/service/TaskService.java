package mate.academy.demo.service;

import mate.academy.demo.dto.task.CreateTaskRequestDto;
import mate.academy.demo.dto.task.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDto save(CreateTaskRequestDto taskRequestDto);

    Page<TaskDto> findAllByProjectId(Long projectId, Pageable pageable);

    TaskDto findById(Long id);

    TaskDto update(CreateTaskRequestDto createTaskRequestDto, Long id);

    void delete(Long id);

    boolean existById(Long id);
}
