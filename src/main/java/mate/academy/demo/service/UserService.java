package mate.academy.demo.service;

import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;

public interface UserService {
    UserDto create(CreateUserRequestDto userRequestDto);

    boolean existById(Long id);
}
