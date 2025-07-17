package mate.academy.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.project.CreateProjectRequestDto;
import mate.academy.demo.dto.project.ProjectDto;
import mate.academy.demo.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Endpoints for managing projects")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "Create project", description = "Create a new project")
    @PostMapping
    public ProjectDto save(@Valid @RequestBody CreateProjectRequestDto requestDto) {
        return projectService.save(requestDto);
    }

    @Operation(summary = "Get project by ID", description = "Retrieve a project by its ID")
    @GetMapping("/{id}")
    public ProjectDto findById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @Operation(summary = "Get all projects",
            description = "Retrieve a paginated list of all projects")
    @GetMapping
    public Page<ProjectDto> findAll(Pageable pageable) {
        return projectService.findAll(pageable);
    }

    @Operation(summary = "Update project", description = "Update an existing project by ID")
    @PutMapping("/{id}")
    public ProjectDto update(@Valid @RequestBody CreateProjectRequestDto requestDto,
                      @PathVariable Long id) {
        return projectService.update(requestDto, id);
    }

    @Operation(summary = "Delete project", description = "Delete a project by ID")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        projectService.delete(id);
    }
}
