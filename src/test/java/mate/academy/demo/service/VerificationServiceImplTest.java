package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getFirstProject;
import static mate.academy.demo.util.TestUtil.getFirstTask;
import static mate.academy.demo.util.TestUtil.getFirstUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.exception.ForbiddenOperationException;
import mate.academy.demo.repository.TaskRepository;
import mate.academy.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerificationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private VerificationServiceImpl verificationService;

    @Test
    @DisplayName("""
            check if user related to project
            """)
    void isCurrentUserRelatedToProject_WithValidId() {
        Long userId = 1L;
        Long projectId = getFirstProject().getId();

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(getFirstUser()));
        when(authenticationService.getCurrentUserId()).thenReturn(userId);

        verificationService.isCurrentUserRelatedToProject(projectId);

        verify(userRepository).findById(anyLong());
        verify(authenticationService).getCurrentUserId();
    }

    @Test
    @DisplayName("""
            check if user related to project with non exist user id
            """)
    void isCurrentUserRelatedToProject_WithInValidId() {
        Long userId = 1L;
        String expectedMessage = "Can't find user with id: " + userId;

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> verificationService.isCurrentUserRelatedToProject(userId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(authenticationService).getCurrentUserId();
        verify(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            check if user related to project with invalid id
            """)
    void isCurrentUserRelatedToProject_WithInValidProject() {
        Long userId = 1L;
        Long projectId = 100L;
        String expectedMessage = "User with ID "
                + userId + " is not a member of project with ID "
                + projectId + " and cannot perform this operation.";

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(getFirstUser()));

        ForbiddenOperationException actual = assertThrows(ForbiddenOperationException.class,
                () -> verificationService.isCurrentUserRelatedToProject(projectId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            check if user related to task
            """)
    void isCurrentUserRelatedToTask_WithValidData() {
        Long userId = 1L;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(getFirstTask()));
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(getFirstUser()));

        verificationService.isCurrentUserRelatedToTask(anyLong());

        verify(taskRepository).findById(anyLong());
        verify(authenticationService).getCurrentUserId();
        verify(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("""
            check if user related to task with invalid id
            """)
    void isCurrentUserRelatedToTask_WithInValidTaskId() {
        Long taskId = 1L;
        String expectedMessage = "Can't find task with id: " + taskId;
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> verificationService.isCurrentUserRelatedToTask(taskId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(taskRepository).findById(anyLong());
    }
}
