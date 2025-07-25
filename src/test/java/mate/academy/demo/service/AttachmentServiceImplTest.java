package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getFirstAttachment;
import static mate.academy.demo.util.TestUtil.getFirstAttachmentDto;
import static mate.academy.demo.util.TestUtil.getFirstTask;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dropbox.core.DbxException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import mate.academy.demo.dto.attachment.AttachmentDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.AttachmentMapper;
import mate.academy.demo.model.Attachment;
import mate.academy.demo.repository.AttachmentRepository;
import mate.academy.demo.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceImplTest {
    @Mock
    private VerificationService verificationService;
    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private AttachmentMapper attachmentMapper;
    @Mock
    private DropboxService dropboxService;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Test
    @DisplayName("""
            find attachments by task id
            """)
    void findByTaskId_WithValidTask_ShouldReturnAttachmentDto() {
        Pageable pageable = PageRequest.of(0, 10);

        Long taskId = getFirstAttachmentDto().getTaskId();
        Page<Attachment> attachmentsPage = new PageImpl<>(List.of(getFirstAttachment()),
                pageable, 1L);
        when(attachmentRepository.findByTaskId(taskId, pageable)).thenReturn(attachmentsPage);
        when(attachmentMapper.toDto(getFirstAttachment())).thenReturn(getFirstAttachmentDto());

        byte[] content = getFirstAttachmentDto().getFile();
        InputStream mockInputStream = new ByteArrayInputStream(content);
        when(dropboxService.downloadFile(getFirstAttachment().getDropBoxFileId()))
                .thenReturn(mockInputStream);

        Page<AttachmentDto> expected = new PageImpl<>(List.of(getFirstAttachmentDto()),
                pageable, 1L);

        Page<AttachmentDto> actual = attachmentService.findByTaskId(taskId, pageable);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToTask(taskId);
        verify(attachmentRepository).findByTaskId(taskId, pageable);
        verify(attachmentMapper).toDto(getFirstAttachment());
        verify(dropboxService).downloadFile(getFirstAttachment().getDropBoxFileId());
    }

    @Test
    @DisplayName("""
            find attachments by non exist task id
            """)
    void findByTaskId_WithNonExistTask_ShouldReturnEmptyPage() {
        Page<AttachmentDto> expected = Page.empty();

        Long taskId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        when(attachmentRepository.findByTaskId(taskId, pageable)).thenReturn(Page.empty());

        Page<AttachmentDto> actual = attachmentService.findByTaskId(taskId, pageable);

        assertEquals(expected, actual);

        verify(attachmentRepository).findByTaskId(taskId, pageable);
    }

    @Test
    @DisplayName("""
            save attachment with valid data
            """)
    void save_WithValidData_ShouldReturnValidDto() throws IOException, DbxException {
        AttachmentDto expected = getFirstAttachmentDto();

        Long taskId = 1L;
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "file.txt", "text/plain", "test data".getBytes()
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(getFirstTask()));
        when(dropboxService.uploadFile(any(InputStream.class), eq(multipartFile.getName())))
                .thenReturn("dbx123abc");
        when(attachmentRepository.save(any(Attachment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(attachmentMapper.toDto(any(Attachment.class))).thenReturn(expected);

        AttachmentDto actual = attachmentService.save(multipartFile, taskId);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToTask(taskId);
        verify(taskRepository).findById(taskId);
        verify(dropboxService).uploadFile(any(InputStream.class), eq(multipartFile.getName()));
        verify(attachmentRepository).save(any(Attachment.class));
        verify(attachmentMapper).toDto(any(Attachment.class));
    }

    @Test
    @DisplayName("""
            save attachment with non exist task
            """)
    void save_WithNonExistTask_ShouldThrowException() {
        String expectedMessage = "Task not found";

        Long taskId = 1L;
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "file.txt", "text/plain", "test data".getBytes()
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> attachmentService.save(multipartFile, taskId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("""
            save attachment with not valid dropbox file id
            """)
    void save_WithNotValidFileId_ShouldThrowException() throws IOException, DbxException {
        String expectedMessage = "Failed to upload file";

        Long taskId = 1L;
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "file.txt", "text/plain", "test data".getBytes()
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(getFirstTask()));
        when(dropboxService.uploadFile(any(InputStream.class), eq(multipartFile.getName())))
                .thenThrow(new RuntimeException());

        RuntimeException actual = assertThrows(RuntimeException.class, () -> attachmentService
                .save(multipartFile, taskId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(verificationService).isCurrentUserRelatedToTask(taskId);
        verify(taskRepository).findById(taskId);
        verify(dropboxService).uploadFile(any(InputStream.class), eq(multipartFile.getName()));
    }
}
