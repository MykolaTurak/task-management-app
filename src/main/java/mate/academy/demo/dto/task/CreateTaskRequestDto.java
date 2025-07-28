package mate.academy.demo.dto.task;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mate.academy.demo.model.Priority;
import mate.academy.demo.model.TaskStatus;

@Getter
@Setter
@EqualsAndHashCode
public class CreateTaskRequestDto {
    @NotBlank(message = "Task name must not be blank")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "Priority must not be null")
    private Priority priority;

    @NotNull(message = "Status must not be null")
    private TaskStatus status;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    @NotNull(message = "Project ID must not be null")
    @Positive(message = "Task ID must be greater than 0")
    private Long projectId;

    @NotNull(message = "Assignee ID must not be null")
    private Long assigneeId;
}
