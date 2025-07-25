package mate.academy.demo.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.project.ProjectDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.model.Task;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final TelegramService telegramService;

    @Value("${telegram.bot.token}")
    private String botToken;

    public void sendTaskNotification(Task task, Long projectId, Long assigneeId) {
        UserDto assigneeDto = userService.findById(assigneeId);
        ProjectDto projectDto = projectService.findById(projectId);

        String message = String.format(
                "✍ New task created!\n\n📌 *%s*\n📂 "
                        + "Project: %s\n👤 Assignee: %s %s \n⏰ Deadline: %s",
                task.getName(), projectDto.getName(), assigneeDto.getFirstName(),
                assigneeDto.getLastName(),
                task.getDueDate()
        );

        User user = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: " + assigneeId
                ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("chat_id", user.getTelegramChatId());
        payload.put("text", message);
        payload.put("parse_mode", "Markdown");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = telegramService.getRestTemplate();
        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);
        restTemplate.postForObject(url, request, String.class);
    }
}
