package mate.academy.demo.service;

import mate.academy.demo.dto.role.UpdateRoleRequestDto;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.dto.user.UserWithRolesDto;

public interface UserService {
    UserDto create(CreateUserRequestDto userRequestDto);

    boolean existById(Long id);

    UserDto getCurrentUser();

    UserDto updateCurrentUser(CreateUserRequestDto createUserRequestDto);

    UserWithRolesDto changeUserRole(Long userId, UpdateRoleRequestDto requestDto);

    UserDto findById(Long userId);
}
