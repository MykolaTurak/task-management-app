package mate.academy.demo.repository;

import static mate.academy.demo.util.TestUtil.getFirstComment;
import static org.junit.Assert.assertEquals;

import java.util.List;
import mate.academy.demo.config.CustomMySqlContainer;
import mate.academy.demo.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Container
    private static MySQLContainer<?> mysql = CustomMySqlContainer.getInstance()
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("testdb");

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find comments by task id
            """)
    void findAllByTaskId_WithValidTask_ShouldReturnValidComments() {
        List<Comment> expected = List.of(getFirstComment());

        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> actual = commentRepository.findAllByTaskId(1L, pageable).toList();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find comments by non exist task id
            """)
    void findAllByTaskId_WithNonExistTask_ShouldReturnEmptyList() {
        List<Comment> expected = List.of();

        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> actual = commentRepository.findAllByTaskId(1L, pageable).toList();

        assertEquals(expected, actual);
    }
}
