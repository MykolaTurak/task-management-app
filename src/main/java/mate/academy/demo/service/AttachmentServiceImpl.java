package mate.academy.demo.service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.attachment.AttachmentDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.AttachmentMapper;
import mate.academy.demo.model.Attachment;
import mate.academy.demo.model.Task;
import mate.academy.demo.repository.AttachmentRepository;
import mate.academy.demo.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final DropboxService dropboxService;
    private final TaskRepository taskRepository;
    private final VerificationService verificationService;

    @Override
    public Page<AttachmentDto> findByTaskId(Long taskId, Pageable pageable) {
        verificationService.isCurrentUserRelatedToTask(taskId);

        return attachmentRepository.findByTaskId(taskId, pageable)
                .map(attachment -> {
                    AttachmentDto attachmentDto = attachmentMapper.toDto(attachment);
                    try {
                        attachmentDto.setFile(dropboxService
                                .downloadFile(attachment.getDropBoxFileId()).readAllBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Can't read drop box file", e);
                    }
                    return attachmentDto;
                });

    }

    @Override
    @Transactional
    public AttachmentDto save(MultipartFile file, Long taskId) {
        verificationService.isCurrentUserRelatedToTask(taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        try (InputStream inputStream = file.getInputStream()) {
            String fileId = dropboxService.uploadFile(inputStream, file.getName());
            Attachment attachment = new Attachment();
            attachment.setTask(task);
            attachment.setDropBoxFileId(fileId);
            attachment.setFilename(file.getOriginalFilename());
            attachment.setUpload(LocalDateTime.now());

            return attachmentMapper.toDto(attachmentRepository.save(attachment));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
