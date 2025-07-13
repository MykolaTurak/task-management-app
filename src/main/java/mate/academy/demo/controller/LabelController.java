package mate.academy.demo.controller;

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
public class LabelController {
    private final LabelService labelService;

    @PostMapping
    LabelDto save(@RequestBody CreateLabelRequestDto requestDto) {
        return labelService.save(requestDto);
    }

    @GetMapping
    Page<LabelDto> findAllByProjectId(@RequestParam Long projectId, Pageable pageable) {
        return labelService.findAllByProjectId(projectId, pageable);
    }

    @PutMapping("/{id}")
    LabelDto update(@RequestBody CreateLabelRequestDto requestDto,
                    @PathVariable Long id) {
        return labelService.update(requestDto, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
