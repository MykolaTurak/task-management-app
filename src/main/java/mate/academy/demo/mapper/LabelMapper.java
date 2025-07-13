package mate.academy.demo.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.label.CreateLabelRequestDto;
import mate.academy.demo.dto.label.LabelDto;
import mate.academy.demo.model.Label;
import mate.academy.demo.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {
    @Mapping(target = "projects", source = "projectsId", qualifiedByName = "mapProjectIdToProject")
    Label toModel(CreateLabelRequestDto requestDto);

    @Mapping(target = "projectsId", source = "projects", qualifiedByName = "mapProjectToProjectId")
    LabelDto toDto(Label label);

    void updateModelFromDto(CreateLabelRequestDto requestDto,
                            @MappingTarget Label label);

    @Named("mapProjectIdToProject")
    default Set<Project> mapProjectIdToProject(Set<Long> projectId) {
        return projectId.stream()
                .map(id -> {
                    Project project = new Project();
                    project.setId(id);
                    return project;
                }).collect(Collectors.toSet());
    }

    @Named("mapProjectToProjectId")
    default Set<Long> mapProjectToProjectId(Set<Project> projects) {
        return projects.stream()
                .map(Project::getId)
                .collect(Collectors.toSet());
    }
}
