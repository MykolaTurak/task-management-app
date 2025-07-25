package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getFirstTask;
import static mate.academy.demo.util.TestUtil.getTaskDto;
import static mate.academy.demo.util.TestUtil.getTaskRequestDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.demo.dto.task.TaskDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.TaskMapper;
import mate.academy.demo.model.Task;
import mate.academy.demo.repository.TaskRepository;
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
class TaskServiceImplTest {
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TelegramService telegramService;
    @Mock
    private TelegramNotificationService telegramNotificationService;
    @Mock
    private ProjectService projectService;
    @Mock
    private UserService userService;
    @Mock
    private VerificationService verificationService;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("""
            save task with valid data
            """)
    void save_WithValidRequestData_ShouldReturnValidDto() {
        TaskDto expected = getTaskDto();

        when(projectService.existById(anyLong())).thenReturn(true);
        when(userService.existById(anyLong())).thenReturn(true);
        when(taskMapper.toModel(getTaskRequestDto())).thenReturn(getFirstTask());
        when(taskMapper.toDto(getFirstTask())).thenReturn(expected);
        when(taskRepository.save(getFirstTask())).thenReturn(getFirstTask());

        TaskDto actual = taskService.save(getTaskRequestDto());

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(projectService).existById(anyLong());
        verify(userService).existById(anyLong());
        verify(taskMapper).toDto(getFirstTask());
        verify(taskMapper).toModel(getTaskRequestDto());
        verify(telegramNotificationService).sendTaskNotification(any(),
                anyLong(), anyLong());
    }

    @Test
    @DisplayName("""
            save task with non exist project id
            """)
    void save_WithNonExistProject_ShouldThrowException() {
        Long projectId = 1L;
        String expectedMessage = "Can't find project with id: " + projectId;

        when(projectService.existById(projectId)).thenReturn(false);

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> taskService.save(getTaskRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(projectService).existById(projectId);
    }

    @Test
    @DisplayName("""
            save task with non exist assignee id
            """)
    void save_WithNonExistAssignee_ShouldThrowException() {
        Long assigneeId = 1L;
        String expectedMessage = "Can't find user with id: " + assigneeId;

        when(projectService.existById(anyLong())).thenReturn(true);
        when(userService.existById(assigneeId)).thenReturn(false);

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> taskService.save(getTaskRequestDto()));

        assertEquals(expectedMessage, actual.getMessage());

        verify(projectService).existById(anyLong());
        verify(userService).existById(assigneeId);
    }

    @Test
    @DisplayName("""
            find all by project id
            """)
    void findAllByProjectId_WithValidProjectId_ShouldReturnValidPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TaskDto> expected = new PageImpl<>(List.of(getTaskDto()), pageable, 1);
        Page<Task> tasks = new PageImpl<>(List.of(getFirstTask()), pageable, 1);

        Long projectId = 1L;
        when(taskRepository.findAllByProjectId(projectId, pageable)).thenReturn(tasks);
        when(taskMapper.toDto(getFirstTask())).thenReturn(getTaskDto());

        Page<TaskDto> actual = taskService.findAllByProjectId(projectId, pageable);

        assertEquals(expected, actual);

        verify(taskRepository).findAllByProjectId(projectId, pageable);
        verify(taskMapper).toDto(getFirstTask());
        verify(projectService).existById(anyLong());
        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
    }

    @Test
    @DisplayName("""
            find task by id
            """)
    void findById_WithValidId_ShouldReturnValidDto() {
        TaskDto expected = getTaskDto();
        Long taskId = expected.getId();

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(getFirstTask()));
        when(taskMapper.toDto(getFirstTask())).thenReturn(expected);

        TaskDto actual = taskService.findById(taskId);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(taskRepository).findById(anyLong());
        verify(taskMapper).toDto(getFirstTask());
    }

    @Test
    @DisplayName("""
            find task by non exist id
            """)
    void findById_WithNonExistId_ShouldReturnValidDto() {
        Long taskId = 1L;
        String expectedMessage = "Can't find task with id: " + taskId;

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> taskService.findById(taskId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(taskRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            update task with valid data
            """)
    void update_WithValidData_ShouldReturnValidDto() {
        TaskDto expected = getTaskDto();
        Long taskId = expected.getId();

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(getFirstTask()));
        when(taskRepository.save(getFirstTask())).thenReturn(getFirstTask());
        when(taskMapper.toDto(getFirstTask())).thenReturn(expected);

        TaskDto actual = taskService.update(getTaskRequestDto(), taskId);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(taskMapper).updateModelFromDto(getTaskRequestDto(), getFirstTask());
        verify(taskRepository).findById(anyLong());
        verify(taskRepository).save(getFirstTask());
        verify(taskMapper).toDto(getFirstTask());
    }

    @Test
    @DisplayName("""
            update task by non exist id
            """)
    void update_WithNonExistId_ShouldReturnValidDto() {
        Long taskId = 1L;
        String expectedMessage = "Can't find task with id: " + taskId;

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> taskService.update(getTaskRequestDto(), taskId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(taskRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            delete task by id
            """)
    void delete_WithValidId() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(getFirstTask()));

        taskService.delete(anyLong());

        verify(verificationService).isCurrentUserRelatedToProject(anyLong());
        verify(taskRepository).findById(anyLong());
        verify(taskRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("""
            delete task by non exist id
            """)
    void delete_WithNonExistId_ShouldThrowException() {
        Long taskId = 1L;
        String expectedMessage = "Can't find task with id: " + taskId;

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> taskService.delete(taskId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(taskRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            check if task exist by valid id
            """)
    void existById() {
        when(taskRepository.existsById(anyLong())).thenReturn(true);

        boolean actual = taskService.existById(anyLong());

        assertTrue(actual);

        verify(taskRepository).existsById(anyLong());
    }
}
