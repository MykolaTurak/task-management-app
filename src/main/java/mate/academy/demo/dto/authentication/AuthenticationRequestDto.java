package mate.academy.demo.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
