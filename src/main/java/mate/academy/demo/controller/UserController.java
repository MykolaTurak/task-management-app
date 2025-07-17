package mate.academy.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Users", description = "Endpoints for managing user profile and roles")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get current user",
            description = "Returns the profile of the currently authenticated user")
    @GetMapping("/me")
    public UserDto getCurrentUser() {
        return userService.getCurrentUser();
    }

    @Operation(summary = "Update current user",
            description = "Updates the profile information of the currently authenticated user")
    @PutMapping("/me")
    public UserDto updateCurrentUser(@Valid @RequestBody CreateUserRequestDto requestDto) {
        return userService.updateCurrentUser(requestDto);
    }

    @Operation(summary = "Update user role (ADMIN only)",
            description = "Changes the role(s) of a user by ID. Requires ADMIN privileges")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public UserWithRolesDto updateUserRole(@Valid @RequestBody UpdateRoleRequestDto requestDto,
                                           @PathVariable Long id) {
        return userService.changeUserRole(id, requestDto);
    }
}
