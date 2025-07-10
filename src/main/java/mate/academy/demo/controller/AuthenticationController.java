package mate.academy.demo.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.authentication.AuthenticationDto;
import mate.academy.demo.dto.authentication.AuthenticationRequestDto;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.service.AuthenticationService;
import mate.academy.demo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    UserDto register(@RequestBody CreateUserRequestDto createUserRequestDto) {
        return userService.create(createUserRequestDto);
    }

    @PostMapping("/login")
    AuthenticationDto login(
            @RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return authenticationService.authenticate(authenticationRequestDto);
    }
}
