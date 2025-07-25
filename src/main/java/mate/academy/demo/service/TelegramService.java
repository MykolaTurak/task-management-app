package mate.academy.demo.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String messageText = message.getText();
            Long chatId = message.getChatId();

            if (messageText.equals("/start")) {
                handleStartCommand(chatId);
            } else {
                handleOtherMessages(chatId, messageText);
            }
        }
    }

    private void handleStartCommand(Long chatId) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText("Hello! To receive notifications from our service please "
                + "enter the unique token you received (eg `/link <your_token>`).");
        try {
            execute(response);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't execute response chatId: " + chatId);
        }
    }

    private void handleOtherMessages(Long chatId, String messageText) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());

        if (messageText.startsWith("/link ")) {
            String userToken = messageText.substring("/link ".length()).trim();
            Optional<User> userOpt = userRepository.findByTemporaryToken(userToken);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                user.setTelegramChatId(chatId);
                user.setTemporaryToken(null);
                userRepository.save(user);

                response.setText("Telegram has been successfully connected to account");
            } else {
                response.setText("Invalid token");
            }

        } else {
            response.setText("Can't understand your command. "
                    + "Please, send `/start` or `/link your_token`.");
        }

        try {
            execute(response);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't execute response chatId: " + chatId);
        }
    }

    public String getInfo() {
        Long userId = authenticationService.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: " + userId
                ));

        String token = UUID.randomUUID().toString();
        user.setTemporaryToken(token);

        userRepository.save(user);

        return "To get notifications in Telegram, go to our bot: https://t.me/turak_task_management_bot"
                + " and send message: \"/link + [token]\". Your token: " + token;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
