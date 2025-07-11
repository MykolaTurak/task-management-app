package mate.academy.demo.repository;

import mate.academy.demo.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId")
    Page<Project> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
