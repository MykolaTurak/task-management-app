package mate.academy.demo.dto.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLabelRequestDto {
    @NotBlank(message = "Label name must not be blank")
    private String name;

    @NotBlank(message = "Label color must not be blank")
    private String color;

    @NotEmpty(message = "At least one project ID must be provided")
    private Set<Long> projectsId;
}
