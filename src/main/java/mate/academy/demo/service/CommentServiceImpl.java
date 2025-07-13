package mate.academy.demo.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.CommentMapper;
import mate.academy.demo.model.Comment;
import mate.academy.demo.model.Task;
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
    private final TaskService taskService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Override
    public Page<CommentDto> findAllByTaskId(Long taskId, Pageable pageable) {
        if (!taskService.existById(taskId)) {
            throw new EntityNotFoundException(
                    "Can't find task with id: " + taskId
            );
        }

        return commentRepository.findAllByTaskId(taskId, pageable).map(commentMapper::toDto);
    }

    @Override
    public CommentDto save(CreateCommentRequestDto requestDto) {
        if (!taskService.existById(requestDto.getTaskId())) {
            throw new EntityNotFoundException(
                    "Can't find task with id: " + requestDto.getTaskId()
            );
        }

        User user = new User();
        user.setId(authenticationService.getCurrentUserId());
        Task task = new Task();
        task.setId(requestDto.getTaskId());
        Comment comment = commentMapper.toModel(requestDto);
        comment.setUser(user);
        comment.setTask(task);
        comment.setTimestamp(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
