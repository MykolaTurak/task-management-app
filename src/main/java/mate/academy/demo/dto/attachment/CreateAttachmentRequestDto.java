package mate.academy.demo.dto.attachment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateAttachmentRequestDto {
    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotNull(message = "File must be provided")
    private MultipartFile file;

    @NotBlank(message = "File name must not be blank")
    private String fileName;
}
