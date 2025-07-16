package mate.academy.demo.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.dto.user.UserWithRolesDto;
import mate.academy.demo.model.Role;
import mate.academy.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(CreateUserRequestDto requestDto);

    UserDto toDto(User user);

    void updateModelFromDto(CreateUserRequestDto userRequestDto, @MappingTarget User user);

    @Mapping(target = "roleNames", source = "roles", qualifiedByName = "fromRolesToRoleNames")
    UserWithRolesDto toDtoWithRoles(User user);

    @Named("fromRolesToRoleNames")
    default Set<String> fromRolesToRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
