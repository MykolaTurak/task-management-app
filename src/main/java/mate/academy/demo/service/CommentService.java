package mate.academy.demo.service;

import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Page<CommentDto> findAllByTaskId(Long taskId, Pageable pageable);

    CommentDto save(CreateCommentRequestDto requestDto);

    void delete(Long id);
}
