package mate.academy.demo.repository;

import mate.academy.demo.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabelRepository extends JpaRepository<Label, Long> {
    @Query("FROM Label l JOIN l.projects p WHERE p.id = :projectId")
    public Page<Label> findAllByProjectId(@Param("projectId") Long projectId, Pageable pageable);
}
