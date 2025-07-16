package mate.academy.demo.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.role.UpdateRoleRequestDto;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.dto.user.UserWithRolesDto;
import mate.academy.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserDto getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping("/me")
    public UserDto updateCurrentUser(@RequestBody CreateUserRequestDto requestDto) {
        return userService.updateCurrentUser(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public UserWithRolesDto updateUserRole(@RequestBody UpdateRoleRequestDto requestDto,
                                           @PathVariable Long id) {
        return userService.changeUserRole(id, requestDto);
    }
}
