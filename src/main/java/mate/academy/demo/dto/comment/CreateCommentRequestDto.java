package mate.academy.demo.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDto {
    private Long taskId;
    private String text;
}
