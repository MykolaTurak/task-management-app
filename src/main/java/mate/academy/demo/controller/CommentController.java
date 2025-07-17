package mate.academy.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import mate.academy.demo.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Tag(name = "Comments", description = "Endpoints for managing comments on tasks")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Get comments by task",
            description = "Retrieve paginated list of comments for a specific task")
    @GetMapping
    public Page<CommentDto> findAllByTask(@RequestParam Long taskId, Pageable pageable) {
        return commentService.findAllByTaskId(taskId, pageable);
    }

    @Operation(summary = "Create comment", description = "Add a new comment to a task")
    @PostMapping
    public CommentDto save(@RequestBody CreateCommentRequestDto requestDto) {
        return commentService.save(requestDto);
    }

    @Operation(summary = "Delete comment", description = "Delete a comment by its ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
