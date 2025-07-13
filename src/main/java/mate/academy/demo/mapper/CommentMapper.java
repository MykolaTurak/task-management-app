package mate.academy.demo.mapper;

import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import mate.academy.demo.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    Comment toModel(CreateCommentRequestDto requestDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "taskId", source = "task.id")
    CommentDto toDto(Comment comment);
}
