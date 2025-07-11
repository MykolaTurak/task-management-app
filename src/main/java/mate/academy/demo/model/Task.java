package mate.academy.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE tasks SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Priority priority;
    @Column(nullable = false)
    private TaskStatus status;
    @Column(nullable = false)
    private LocalDateTime dueDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    private User assignee;
    private boolean isDeleted = false;
}
