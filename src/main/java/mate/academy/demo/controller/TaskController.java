package mate.academy.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Tasks", description = "Endpoints for managing tasks within projects")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Create task", description = "Create a new task in a specific project")
    @PostMapping
    public TaskDto save(@Valid @RequestBody CreateTaskRequestDto requestDto) {
        return taskService.save(requestDto);
    }

    @Operation(summary = "Get tasks by project",
            description = "Retrieve paginated list of tasks for a given project")
    @GetMapping
    public Page<TaskDto> findAllByProjectId(@RequestParam Long projectId, Pageable pageable) {
        return taskService.findAllByProjectId(projectId, pageable);
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a task by its ID")
    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @Operation(summary = "Update task", description = "Update task details by task ID")
    @PutMapping("/{id}")
    public TaskDto update(@Valid @RequestBody CreateTaskRequestDto requestDto,
                   @PathVariable Long id) {
        return taskService.update(requestDto, id);
    }

    @Operation(summary = "Delete task", description = "Delete a task by its ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
