package mate.academy.demo.dto.attachment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateAttachmentRequestDto {
    private Long taskId;
    private MultipartFile file;
    private String fileName;
}
