package mate.academy.demo.repository;

import mate.academy.demo.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("FROM Comment c WHERE c.task.id = :taskId")
    Page<Comment> findAllByTaskId(@Param("taskId") Long taskId, Pageable pageable);
}
