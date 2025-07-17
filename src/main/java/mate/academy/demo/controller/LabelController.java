package mate.academy.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.label.CreateLabelRequestDto;
import mate.academy.demo.dto.label.LabelDto;
import mate.academy.demo.service.LabelService;
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
@RequestMapping("/labels")
@Tag(name = "Labels", description = "Endpoints for managing labels within a project")
public class LabelController {
    private final LabelService labelService;

    @Operation(summary = "Create label", description = "Create a new label for a specific project")
    @PostMapping
    LabelDto save(@RequestBody CreateLabelRequestDto requestDto) {
        return labelService.save(requestDto);
    }

    @Operation(summary = "Get labels by project",
            description = "Retrieve paginated list of labels for a given project")
    @GetMapping
    Page<LabelDto> findAllByProjectId(@RequestParam Long projectId, Pageable pageable) {
        return labelService.findAllByProjectId(projectId, pageable);
    }

    @Operation(summary = "Update label", description = "Update label details by label ID")
    @PutMapping("/{id}")
    LabelDto update(@RequestBody CreateLabelRequestDto requestDto,
                    @PathVariable Long id) {
        return labelService.update(requestDto, id);
    }

    @Operation(summary = "Delete label", description = "Delete a label by its ID")
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
