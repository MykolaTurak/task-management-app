package mate.academy.demo.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.CommentMapper;
import mate.academy.demo.model.Comment;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthenticationService authenticationService;
    private final VerificationService verificationService;

    @Override
    public Page<CommentDto> findAllByTaskId(Long taskId, Pageable pageable) {
        verificationService.isCurrentUserRelatedToTask(taskId);

        return commentRepository.findAllByTaskId(taskId, pageable).map(commentMapper::toDto);
    }

    @Override
    public CommentDto save(CreateCommentRequestDto requestDto) {
        verificationService.isCurrentUserRelatedToTask(requestDto.getTaskId());

        User user = new User();
        user.setId(authenticationService.getCurrentUserId());
        Comment comment = commentMapper.toModel(requestDto);
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find comment with id: " + id
                ));
        verificationService.isCurrentUserRelatedToTask(comment.getTask().getId());

        commentRepository.deleteById(id);
    }
}
