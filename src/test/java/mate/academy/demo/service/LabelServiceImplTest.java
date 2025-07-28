package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getFirstLabel;
import static mate.academy.demo.util.TestUtil.getFirstLabelDto;
import static mate.academy.demo.util.TestUtil.getLabelRequestDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.demo.dto.label.LabelDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.LabelMapper;
import mate.academy.demo.model.Label;
import mate.academy.demo.repository.LabelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class LabelServiceImplTest {
    @Mock
    private VerificationService verificationService;
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private ProjectService projectService;
    @Mock
    private LabelMapper labelMapper;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Test
    @DisplayName("""
            save label with valid data
            """)
    void save_WithValidRequestDto_ShouldReturnValidDto() {
        when(projectService.existById(anyLong())).thenReturn(true);
        when(labelMapper.toModel(getLabelRequestDto())).thenReturn(getFirstLabel());
        when(labelMapper.toDto(getFirstLabel())).thenReturn(getFirstLabelDto());
        when(labelRepository.save(getFirstLabel())).thenReturn(getFirstLabel());

        LabelDto expected = getFirstLabelDto();

        LabelDto actual = labelService.save(getLabelRequestDto());

        assertEquals(expected, actual);

        verify(projectService).existById(anyLong());
        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(labelMapper).toDto(getFirstLabel());
        verify(labelMapper).toModel(getLabelRequestDto());
        verify(labelRepository).save(getFirstLabel());
    }

    @Test
    @DisplayName("""
            save label with non exist project id
            """)
    void save_WithNonExistProject_ShouldReturnValidDto() {
        Long projectId = 1L;
        String expectedMessage = "Can't find project with id: " + projectId;

        when(projectService.existById(projectId)).thenReturn(false);

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> labelService.save(getLabelRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(projectService).existById(projectId);
    }

    @Test
    @DisplayName("""
            find labels by project id
            """)
    void findAllByProjectId_WithValidId_ShouldReturnValidPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<LabelDto> expected = new PageImpl<>(List.of(getFirstLabelDto()), pageable, 1);

        Long projectId = 1L;
        Page<Label> labels = new PageImpl<>(List.of(getFirstLabel()), pageable, 1);
        when(labelRepository.findAllByProjectId(1L, pageable)).thenReturn(labels);
        when(labelMapper.toDto(getFirstLabel())).thenReturn(getFirstLabelDto());

        Page<LabelDto> actual = labelService.findAllByProjectId(1L, pageable);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToProject(1L);
        verify(labelMapper).toDto(getFirstLabel());
        verify(labelRepository).findAllByProjectId(1L, pageable);
    }

    @Test
    @DisplayName("""
            update label by id
            """)
    void update_WithValidId_ShouldReturnValidDto() {
        Long labelId = 1L;
        when(labelRepository.findById(labelId)).thenReturn(Optional.of(getFirstLabel()));
        when(labelRepository.save(getFirstLabel())).thenReturn(getFirstLabel());
        when(labelMapper.toDto(getFirstLabel())).thenReturn(getFirstLabelDto());

        LabelDto expected = getFirstLabelDto();

        LabelDto actual = labelService.update(getLabelRequestDto(), labelId);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(labelRepository).save(getFirstLabel());
        verify(labelRepository).findById(labelId);
        verify(labelMapper).toDto(getFirstLabel());
    }

    @Test
    @DisplayName("""
            update label by non exist id
            """)
    void update_WithNonExistId_ShouldThrowException() {
        Long labelId = 1L;
        String expectedMessage = "Can't find label with id: " + labelId;

        when(labelRepository.findById(labelId)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> labelService.update(getLabelRequestDto(), labelId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(labelRepository).findById(labelId);
    }

    @Test
    @DisplayName("""
            delete label by id
            """)
    void delete_WithValidId() {
        Long labelId = 1L;
        when(labelRepository.findById(labelId)).thenReturn(Optional.of(getFirstLabel()));

        labelService.delete(labelId);

        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(labelRepository).deleteById(labelId);
        verify(labelRepository).findById(labelId);
    }

    @Test
    @DisplayName("""
            delete label by non exist id
            """)
    void delete_WithNonExistId_ShouldThrowException() {
        Long labelId = 1L;
        String expectedMessage = "Can't find label with id: " + labelId;

        when(labelRepository.findById(labelId)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> labelService.delete(labelId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(labelRepository).findById(labelId);
    }
}
