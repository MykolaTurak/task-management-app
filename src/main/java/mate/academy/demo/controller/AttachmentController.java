package mate.academy.demo.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.attachment.AttachmentDto;
import mate.academy.demo.service.AttachmentService;
import mate.academy.demo.service.DropboxServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;
    private final DropboxServiceImpl dropboxService;

    @PostMapping
    public ResponseEntity<AttachmentDto> upload(
            @RequestParam("taskId") Long taskId,
            @RequestParam("file") MultipartFile file) {
        AttachmentDto dto = attachmentService.save(file, taskId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    Page<AttachmentDto> findByTaskId(@RequestParam Long taskId, Pageable pageable) {
        return attachmentService.findByTaskId(taskId, pageable);
    }
}
