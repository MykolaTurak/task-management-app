package mate.academy.demo.repository;

import static mate.academy.demo.util.TestUtil.getFirstAttachment;
import static mate.academy.demo.util.TestUtil.getSecondAttachment;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import mate.academy.demo.model.Attachment;
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
class AttachmentRepositoryTest {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find attachments by task id
            """)
    void findByTaskId_ValidTask_ShouldReturnValidAttachments() {
        List<Attachment> expected = List.of(getFirstAttachment(), getSecondAttachment());

        Pageable pageable = PageRequest.of(0, 10);
        List<Attachment> actual = attachmentRepository.findByTaskId(1L, pageable).toList();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Find attachments by non exist task id
            """)
    void findByTaskId_NonExistTask_ShouldReturnEmptyPage() {
        List<Attachment> expected = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 10);
        List<Attachment> actual = attachmentRepository.findByTaskId(1L, pageable).toList();

        assertEquals(expected, actual);
    }
}
