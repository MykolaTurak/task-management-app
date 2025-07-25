package mate.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

class TelegramServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TelegramService telegramService;

    private TelegramService spyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spyService = spy(telegramService);
    }

    @Test
    void onUpdateReceived_startCommand_sendsWelcomeMessage() throws TelegramApiException {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(123L);
        message.setChat(chat);
        message.setText("/start");
        Update update = new Update();
        update.setMessage(message);

        doAnswer(invocation -> null).when(spyService).execute(any(SendMessage.class));

        spyService.onUpdateReceived(update);

        verify(spyService).execute(argThat((SendMessage sendMessage) ->
                sendMessage.getChatId().equals("123")
                        && sendMessage.getText().contains("Hello!")));
    }

    @Test
    void onUpdateReceived_linkValidToken_savesUserAndSendsSuccessMessage()
            throws TelegramApiException {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(456L);
        message.setChat(chat);
        message.setText("/link valid-token");
        Update update = new Update();
        update.setMessage(message);

        User user = new User();
        user.setTemporaryToken("valid-token");
        when(userRepository.findByTemporaryToken("valid-token")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        doAnswer(invocation -> null).when(spyService).execute(any(SendMessage.class));

        spyService.onUpdateReceived(update);

        assertEquals(456L, user.getTelegramChatId());
        assertNull(user.getTemporaryToken());

        verify(spyService).execute(argThat((SendMessage sendMessage) ->
                sendMessage.getChatId().equals("456")
                        && sendMessage.getText()
                        .equals("Telegram has been successfully connected to account")));
    }

    @Test
    void onUpdateReceived_linkInvalidToken_sendsInvalidTokenMessage() throws TelegramApiException {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(789L);
        message.setChat(chat);
        message.setText("/link invalid-token");
        Update update = new Update();
        update.setMessage(message);

        when(userRepository.findByTemporaryToken("invalid-token")).thenReturn(Optional.empty());

        doAnswer(invocation -> null).when(spyService).execute(any(SendMessage.class));

        spyService.onUpdateReceived(update);

        verify(spyService).execute(argThat((SendMessage sendMessage) ->
                sendMessage.getChatId().equals("789")
                        && sendMessage.getText().equals("Invalid token")));
    }

    @Test
    void onUpdateReceived_unrecognizedCommand_sendsCantUnderstandMessage()
            throws TelegramApiException {
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(101L);
        message.setChat(chat);
        message.setText("hello");
        Update update = new Update();
        update.setMessage(message);

        doAnswer(invocation -> null).when(spyService).execute(any(SendMessage.class));

        spyService.onUpdateReceived(update);

        verify(spyService).execute(argThat((SendMessage sendMessage) ->
                sendMessage.getChatId().equals("101")
                        && sendMessage.getText().contains("Can't understand your command")));
    }

    @Test
    void getInfo_existingUser_generatesTokenAndReturnsMessage() {
        User user = new User();
        user.setId(1L);
        when(authenticationService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String infoMessage = telegramService.getInfo();

        assertTrue(infoMessage.contains("To get notifications in Telegram"));
        assertNotNull(user.getTemporaryToken());
        assertTrue(infoMessage.contains(user.getTemporaryToken()));
    }

    @Test
    void getInfo_userNotFound_throwsException() {
        when(authenticationService.getCurrentUserId()).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class, telegramService::getInfo);

        assertTrue(thrown.getMessage().contains("Can't find user with id: 2"));
    }

    @Test
    void getBotUsername_returnsUsername() {
        ReflectionTestUtils.setField(telegramService, "botUsername", "test_bot");
        assertEquals("test_bot", telegramService.getBotUsername());
    }

    @Test
    void getBotToken_returnsToken() {
        ReflectionTestUtils.setField(telegramService, "botToken", "test_token");
        assertEquals("test_token", telegramService.getBotToken());
    }

    @Test
    void getRestTemplate_returnsNewInstance() {
        assertNotNull(telegramService.getRestTemplate());
        assertTrue(telegramService.getRestTemplate()
                instanceof org.springframework.web.client.RestTemplate);
    }
}
