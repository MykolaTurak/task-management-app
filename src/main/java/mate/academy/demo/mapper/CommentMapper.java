package mate.academy.demo.mapper;

import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import mate.academy.demo.model.Comment;
import mate.academy.demo.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "task", source = "taskId", qualifiedByName = "fromTaskIdToTask")
    Comment toModel(CreateCommentRequestDto requestDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "taskId", source = "task.id")
    CommentDto toDto(Comment comment);

    @Named("fromTaskIdToTask")
    default Task fromTaskIdToTask(Long projectId) {
        Task task = new Task();
        task.setId(projectId);

        return task;
    }
}

