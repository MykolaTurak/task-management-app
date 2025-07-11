package mate.academy.demo.dto.project;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import mate.academy.demo.model.ProjectStatus;

@Getter
@Setter
public class CreateProjectRequestDto {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
}
