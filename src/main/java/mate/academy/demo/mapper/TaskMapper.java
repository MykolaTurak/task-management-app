package mate.academy.demo.mapper;

import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.task.CreateTaskRequestDto;
import mate.academy.demo.dto.task.TaskDto;
import mate.academy.demo.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {
    Task toModel(CreateTaskRequestDto requestDto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "projectId", source = "project.id")
    TaskDto toDto(Task task);

    void updateModelFromDto(CreateTaskRequestDto requestDto, @MappingTarget Task task);
}
