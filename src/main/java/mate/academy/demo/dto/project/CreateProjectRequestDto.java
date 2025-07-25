package mate.academy.demo.dto.project;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mate.academy.demo.model.ProjectStatus;

@Getter
@Setter
@EqualsAndHashCode
public class CreateProjectRequestDto {
    @NotBlank(message = "Project name must not be blank")
    private String name;

    @NotBlank(message = "Project description must not be blank")
    private String description;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in the present or future")
    private LocalDateTime endDate;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;
}
