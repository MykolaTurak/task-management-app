package mate.academy.demo.mapper;

import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.project.CreateProjectRequestDto;
import mate.academy.demo.dto.project.ProjectDto;
import mate.academy.demo.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ProjectMapper {
    Project toModel(CreateProjectRequestDto requestDto);

    ProjectDto toDto(Project project);

    void updateModelFromDto(CreateProjectRequestDto requestDto, @MappingTarget Project book);

}
