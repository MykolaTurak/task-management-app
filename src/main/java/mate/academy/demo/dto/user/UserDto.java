package mate.academy.demo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
}
