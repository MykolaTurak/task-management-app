package mate.academy.demo.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.task.CreateTaskRequestDto;
import mate.academy.demo.dto.task.TaskDto;
import mate.academy.demo.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    TaskDto save(@RequestBody CreateTaskRequestDto requestDto) {
        return taskService.save(requestDto);
    }

    @GetMapping
    Page<TaskDto> findAllByProjectId(@RequestParam Long projectId, Pageable pageable) {
        return taskService.findAllByProjectId(projectId, pageable);
    }

    @GetMapping("/{id}")
    TaskDto findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PutMapping("/{id}")
    TaskDto update(@RequestBody CreateTaskRequestDto requestDto,
                   @PathVariable Long id) {
        return taskService.update(requestDto, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
