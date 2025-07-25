package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getFirstTask;
import static mate.academy.demo.util.TestUtil.getFirstUser;
import static mate.academy.demo.util.TestUtil.getProjectDto;
import static mate.academy.demo.util.TestUtil.getUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private ProjectService projectService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private TelegramNotificationService telegramNotificationService;

    @Test
    @DisplayName("""
            send task notification
            """)
    void sendTaskNotification_WithValidData() {
        when(userService.findById(anyLong())).thenReturn(getUserDto());
        when(projectService.findById(anyLong())).thenReturn(getProjectDto());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(getFirstUser()));
        when(telegramService.getRestTemplate()).thenReturn(restTemplate);

        Long projectId = 1L;
        Long assigneeId = 1L;
        telegramNotificationService.sendTaskNotification(getFirstTask(), projectId, assigneeId);

        verify(userService).findById(anyLong());
        verify(userRepository).findById(anyLong());
        verify(projectService).findById(anyLong());
        verify(restTemplate).postForObject(anyString(), any(HttpEntity.class), eq(String.class));
    }
}
