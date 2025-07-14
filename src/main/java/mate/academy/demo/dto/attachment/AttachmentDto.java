package mate.academy.demo.dto.attachment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentDto {
    private Long taskId;
    private byte[] file;
    private String filename;
}
