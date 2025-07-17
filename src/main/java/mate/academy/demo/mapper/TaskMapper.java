package mate.academy.demo.mapper;

import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.task.CreateTaskRequestDto;
import mate.academy.demo.dto.task.TaskDto;
import mate.academy.demo.model.Project;
import mate.academy.demo.model.Task;
import mate.academy.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {
    @Mapping(target = "assignee", source = "assigneeId",
            qualifiedByName = "fromAssigneeIdToAssignee")
    @Mapping(target = "project", source = "projectId", qualifiedByName = "fromProjectIdToProject")
    Task toModel(CreateTaskRequestDto requestDto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "projectId", source = "project.id")
    TaskDto toDto(Task task);

    void updateModelFromDto(CreateTaskRequestDto requestDto, @MappingTarget Task task);

    @Named("fromAssigneeIdToAssignee")
    default User fromAssigneeIdToAssignee(Long assigneeId) {
        User assignee = new User();
        assignee.setId(assigneeId);
        return assignee;
    }

    @Named("fromProjectIdToProject")
    default Project fromProjectIdToProject(Long projectId) {
        Project project = new Project();
        project.setId(projectId);

        return project;
    }
}
