package mate.academy.demo.service;

import mate.academy.demo.dto.attachment.AttachmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Page<AttachmentDto> findByTaskId(Long taskId, Pageable pageable);

    AttachmentDto save(MultipartFile file, Long taskId);
}
