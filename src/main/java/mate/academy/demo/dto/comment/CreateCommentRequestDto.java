package mate.academy.demo.dto.comment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDto {
    @NotNull(message = "Task ID must not be null")
    @Min(value = 1, message = "Task ID must be greater than 0")
    private Long taskId;

    @NotBlank(message = "Comment text must not be blank")
    private String text;
}
