package mate.academy.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Attachments", description = "Endpoints for uploading and retrieving attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;
    private final DropboxServiceImpl dropboxService;

    @Operation(
            summary = "Upload attachment",
            description = "Upload a file to Dropbox and store it in"
                    + " the system under a specific task ID."
    )
    @PostMapping
    public ResponseEntity<AttachmentDto> upload(
            @RequestParam("taskId") Long taskId,
            @RequestParam("file") MultipartFile file) {
        AttachmentDto dto = attachmentService.save(file, taskId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get attachments by task ID",
            description = "Retrieve a paginated list of attachments associated"
                    + " with the specified task."
    )
    @GetMapping
    Page<AttachmentDto> findByTaskId(@RequestParam Long taskId, Pageable pageable) {
        return attachmentService.findByTaskId(taskId, pageable);
    }
}
