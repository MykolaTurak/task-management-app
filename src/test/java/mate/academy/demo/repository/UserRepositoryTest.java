package mate.academy.demo.repository;

import static mate.academy.demo.util.TestUtil.getFirstUser;
import static mate.academy.demo.util.TestUtil.getThirdUser;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import mate.academy.demo.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private static final String NON_EXIST_EMAIL = "non.exist@example.com";
    private static final String NON_EXIST_TOKEN = "non_exist_token";

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find user by email
            """)
    void findByEmail_WithValidEmail_ShouldReturnValidUser() {
        Optional<User> expected = Optional.of(getFirstUser());

        Optional<User> actual = userRepository.findByEmail(getFirstUser().getEmail());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find user by non exist email
            """)
    void findByEmail_WithNonExistEmail_ShouldReturnEmptyUser() {
        Optional<Object> expected = Optional.empty();

        Optional<User> actual = userRepository.findByEmail(NON_EXIST_EMAIL);

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find user by token
            """)
    void findByTemporaryToken_WithValidToken_ShouldReturnValidUser() {
        Optional<User> expected = Optional.of(getThirdUser());

        Optional<User> actual = userRepository
                .findByTemporaryToken(getThirdUser().getTemporaryToken());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find user by non exist email
            """)
    void findByToken_WithNonExistToken_ShouldReturnEmptyUser() {
        Optional<Object> expected = Optional.empty();

        Optional<User> actual = userRepository.findByTemporaryToken(NON_EXIST_TOKEN);

        assertEquals(expected, actual);
    }
}
