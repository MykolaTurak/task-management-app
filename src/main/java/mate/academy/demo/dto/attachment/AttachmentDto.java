package mate.academy.demo.dto.attachment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AttachmentDto {
    private Long taskId;
    private byte[] file;
    private String filename;
}
