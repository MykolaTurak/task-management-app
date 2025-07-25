package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getFirstProject;
import static mate.academy.demo.util.TestUtil.getProjectDto;
import static mate.academy.demo.util.TestUtil.getProjectRequestDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.demo.dto.project.CreateProjectRequestDto;
import mate.academy.demo.dto.project.ProjectDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.ProjectMapper;
import mate.academy.demo.model.Project;
import mate.academy.demo.repository.ProjectRepository;
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
class ProjectServiceImplTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private VerificationService verificationService;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName("save project with valid request")
    void save_WithValidRequestDto_ShouldReturnValidDto() {
        ProjectDto expected = getProjectDto();
        CreateProjectRequestDto request = getProjectRequestDto();
        Project project = getFirstProject();

        when(projectMapper.toModel(request)).thenReturn(project);
        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(projectMapper.toDto(project)).thenReturn(expected);
        when(projectRepository.save(project)).thenReturn(project);

        ProjectDto actual = projectService.save(request);

        assertEquals(expected, actual);

        verify(projectMapper).toModel(request);
        verify(projectMapper).toDto(project);
        verify(projectRepository).save(project);
        verify(authenticationService).getCurrentUserId();
    }

    @Test
    @DisplayName("""
            find project by valid id
            """)
    void findById_WithValidId_ShouldReturnDto() {
        ProjectDto expected = getProjectDto();

        Long projectId = expected.getId();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(getFirstProject()));
        when(projectMapper.toDto(getFirstProject())).thenReturn(expected);

        ProjectDto actual = projectService.findById(projectId);

        assertEquals(expected, actual);

        verify(projectRepository).findById(projectId);
        verify(projectMapper).toDto(getFirstProject());
    }

    @Test
    @DisplayName("""
            find project by non exist id
            """)
    void findById_WithNonExistId_ShouldThrowException() {
        Long projectId = 1L;
        String expectedMessage = "Can't find project with id: " + projectId;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> projectService.findById(projectId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("""
            find all projects
            """)
    void findAll_ShouldReturnValidPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> projects = new PageImpl<>(List.of(getFirstProject()), pageable, 1);
        Long userId = 1L;
        when(projectRepository.findAllByUserId(userId, pageable)).thenReturn(projects);
        when(projectMapper.toDto(getFirstProject())).thenReturn(getProjectDto());
        when(authenticationService.getCurrentUserId()).thenReturn(userId);

        Page<ProjectDto> expected = new PageImpl<>(List.of(getProjectDto()), pageable, 1);

        Page<ProjectDto> actual = projectService.findAll(pageable);

        assertEquals(expected, actual);

        verify(projectRepository).findAllByUserId(userId, pageable);
        verify(projectMapper).toDto(getFirstProject());
        verify(authenticationService).getCurrentUserId();
    }

    @Test
    @DisplayName("""
            update project with valid data
            """)
    void update_WithValidData_ShouldReturnValidDto() {
        ProjectDto expected = getProjectDto();
        Long projectId = expected.getId();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(getFirstProject()));
        when(projectRepository.save(getFirstProject())).thenReturn(getFirstProject());
        when(projectMapper.toDto(getFirstProject())).thenReturn(expected);

        ProjectDto actual = projectService.update(getProjectRequestDto(), projectId);

        assertEquals(expected, actual);

        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(getFirstProject());
        verify(projectMapper).updateModelFromDto(getProjectRequestDto(), getFirstProject());
        verify(projectMapper).toDto(getFirstProject());
    }

    @Test
    @DisplayName("""
            update project by non exist id
            """)
    void update_WithNonExistProjectId_ShouldThrowException() {
        Long projectId = 1L;
        String expectedMessage = "Can't find project with id: " + projectId;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> projectService.update(getProjectRequestDto(), projectId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("""
            delete project by valid id
            """)
    void delete_WithValidData() {
        Long projectId = 1L;

        projectService.delete(projectId);

        verify(projectRepository).deleteById(projectId);
        verify(verificationService).isCurrentUserRelatedToProject(projectId);
    }

    @Test
    @DisplayName("""
            check if project exist by id
            """)
    void existById_WithValidId_ShouldReturnTrue() {
        when(projectRepository.existsById(anyLong())).thenReturn(true);

        boolean actual = projectService.existById(anyLong());

        assertTrue(actual);

        verify(projectRepository).existsById(anyLong());
    }
}
