package mate.academy.demo.service;

import static mate.academy.demo.util.TestUtil.getCommentDto;
import static mate.academy.demo.util.TestUtil.getFirstComment;
import static mate.academy.demo.util.TestUtil.getValidCommentRequestDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.CommentMapper;
import mate.academy.demo.model.Comment;
import mate.academy.demo.repository.CommentRepository;
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

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private VerificationService verificationService;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("""
            find comments with task id
            """)
    void findAllByTaskId_WithValidTask_ShouldReturnValidData() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CommentDto> expected = new PageImpl<>(List.of(getCommentDto()), pageable, 1L);

        Long taskId = 1L;
        Page<Comment> comments = new PageImpl<>(List.of(getFirstComment()), pageable, 1L);
        when(commentRepository.findAllByTaskId(taskId, pageable)).thenReturn(comments);
        when(commentMapper.toDto(getFirstComment())).thenReturn(getCommentDto());

        Page<CommentDto> actual = commentService.findAllByTaskId(taskId, pageable);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToTask(taskId);
        verify(commentRepository).findAllByTaskId(taskId, pageable);
        verify(commentMapper).toDto(getFirstComment());
    }

    @Test
    @DisplayName("""
            save comment with valid request dto
            """)
    void save_WithValidRequestDto_ShouldReturnValidDto() {
        CommentDto expected = getCommentDto();

        Comment comment = getFirstComment();
        CreateCommentRequestDto requestDto = getValidCommentRequestDto();
        when(authenticationService.getCurrentUserId()).thenReturn(expected.getUserId());
        when(commentMapper.toModel(requestDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(expected);

        CommentDto actual = commentService.save(requestDto);

        assertEquals(expected, actual);

        verify(verificationService).isCurrentUserRelatedToTask(requestDto.getTaskId());
        verify(authenticationService).getCurrentUserId();
        verify(commentMapper).toModel(requestDto);
        verify(commentMapper).toDto(comment);
        verify(commentRepository).save(comment);
    }

    @Test
    @DisplayName("""
            delete comment with valid id
            """)
    void delete_WithValidCommentId() {
        Long commentId = getFirstComment().getId();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(getFirstComment()));

        commentService.delete(commentId);

        verify(verificationService).isCurrentUserRelatedToTask(getFirstComment().getTask().getId());
        verify(commentRepository).findById(commentId);
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    @DisplayName("""
            delete comment with non exist id
            """)
    void delete_WithNonExistCommentId_ShouldThrowException() {
        Long commentId = 1L;
        String expectedMessage = "Can't find comment with id: " + commentId;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> commentService.delete(commentId));

        assertEquals(expectedMessage, actual.getMessage());

        verify(commentRepository).findById(commentId);
    }
}
