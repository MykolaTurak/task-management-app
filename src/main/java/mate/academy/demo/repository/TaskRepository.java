package mate.academy.demo.repository;

import mate.academy.demo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("FROM Task t WHERE t.project.id = :projectId")
    Page<Task> findAllByProjectId(@Param("projectId") Long projectId, Pageable pageable);
}
