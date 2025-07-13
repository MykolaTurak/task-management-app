package mate.academy.demo.service;

import mate.academy.demo.dto.label.CreateLabelRequestDto;
import mate.academy.demo.dto.label.LabelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LabelService {
    LabelDto save(CreateLabelRequestDto requestDto);

    Page<LabelDto> findAllByProjectId(Long projectId, Pageable pageable);

    LabelDto update(CreateLabelRequestDto requestDto, Long id);

    void delete(Long id);
}
