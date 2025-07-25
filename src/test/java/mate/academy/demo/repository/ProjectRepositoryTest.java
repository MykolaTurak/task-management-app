package mate.academy.demo.repository;

import static mate.academy.demo.util.TestUtil.getFirstProject;
import static org.junit.Assert.assertEquals;

import java.util.List;
import mate.academy.demo.model.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find projects by user id
            """)

    void findAllByUserId_WithValidUser_ShouldReturnValidProjects() {
        List<Project> expected = List.of(getFirstProject());

        Pageable pageable = PageRequest.of(0, 10);
        List<Project> actual = projectRepository.findAllByUserId(1L, pageable).toList();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find projects by non exist user id
            """)
    void findAllByUserId_WithNonExistUser_ShouldReturnEmptyList() {
        List<Project> expected = List.of();

        Pageable pageable = PageRequest.of(0, 10);
        List<Project> actual = projectRepository.findAllByUserId(1L, pageable).toList();

        assertEquals(expected, actual);
    }
}
