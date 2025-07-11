package mate.academy.demo.dto.task;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import mate.academy.demo.model.Priority;
import mate.academy.demo.model.TaskStatus;

@Getter
@Setter
public class CreateTaskRequestDto {
    private String name;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private Long projectId;
    private Long assigneeId;
}
