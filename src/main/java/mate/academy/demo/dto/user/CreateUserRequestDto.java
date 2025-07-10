package mate.academy.demo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequestDto {
    private String userName;
    private String password;
    private String repeatPassword;
    private String email;
    private String firstName;
    private String lastName;
}
