package mate.academy.demo.repository;

import mate.academy.demo.model.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("FROM Attachment a WHERE a.task.id = :taskId")
    Page<Attachment> findByTaskId(@Param("taskId") Long taskId, Pageable pageable);
}
