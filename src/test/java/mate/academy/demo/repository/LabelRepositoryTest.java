package mate.academy.demo.repository;

import static mate.academy.demo.util.TestUtil.getFirstLabel;
import static mate.academy.demo.util.TestUtil.getSecondLabel;
import static org.junit.Assert.assertEquals;

import java.util.List;
import mate.academy.demo.model.Label;
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
class LabelRepositoryTest {
    @Autowired
    private LabelRepository labelRepository;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find labels by project id
            """)
    void findAllByProjectId_WithValidProject_ShouldReturnValidLabels() {
        List<Label> expected = List.of(getFirstLabel(), getSecondLabel());

        Pageable pageable = PageRequest.of(0, 10);
        List<Label> actual = labelRepository.findAllByProjectId(1L, pageable).toList();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find labels by non exist project id
            """)
    void findAllByProjectId_WithNonExistProject_ShouldReturnEmptyList() {
        List<Label> expected = List.of();

        Pageable pageable = PageRequest.of(0, 10);
        List<Label> actual = labelRepository.findAllByProjectId(1L, pageable).toList();

        assertEquals(expected, actual);
    }
}
