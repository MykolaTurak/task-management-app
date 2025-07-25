package mate.academy.demo.repository;

import static mate.academy.demo.util.TestUtil.getFirstTask;
import static mate.academy.demo.util.TestUtil.getFourthTask;
import static org.junit.Assert.assertEquals;

import java.util.List;
import mate.academy.demo.config.CustomMySqlContainer;
import mate.academy.demo.model.Task;
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
class TaskRepositoryTest {
    @Container
    private static MySQLContainer<?> mysql = CustomMySqlContainer.getInstance()
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("testdb");

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find labels by project id
            """)
    void findAllByProjectId_WithValidProject_ShouldReturnValidTasks() {
        List<Task> expected = List.of(getFirstTask(), getFourthTask());

        Pageable pageable = PageRequest.of(0, 10);
        List<Task> actual = taskRepository.findAllByProjectId(1L, pageable).toList();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find labels by project id
            """)
    void findAllByProjectId_WithNonExistProject_ShouldReturnEmptyList() {
        List<Task> expected = List.of();

        Pageable pageable = PageRequest.of(0, 10);
        List<Task> actual = taskRepository.findAllByProjectId(1L, pageable).toList();

        assertEquals(expected, actual);
    }
}
