package mate.academy.demo.service;

import mate.academy.demo.dto.authentication.AuthenticationDto;
import mate.academy.demo.dto.authentication.AuthenticationRequestDto;

public interface AuthenticationService {
    AuthenticationDto authenticate(AuthenticationRequestDto requestDto);

    Long getCurrentUserId();
}
