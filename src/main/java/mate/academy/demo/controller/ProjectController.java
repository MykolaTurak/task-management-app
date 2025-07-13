package mate.academy.demo.controller;

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
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ProjectDto save(@RequestBody CreateProjectRequestDto requestDto) {
        return projectService.save(requestDto);
    }

    @GetMapping("/{id}")
    public ProjectDto findById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @GetMapping
    public Page<ProjectDto> findAll(Pageable pageable) {
        return projectService.findAll(pageable);
    }

    @PutMapping("/{id}")
    public ProjectDto update(@RequestBody CreateProjectRequestDto requestDto,
                      @PathVariable Long id) {
        return projectService.update(requestDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        projectService.delete(id);
    }
}
