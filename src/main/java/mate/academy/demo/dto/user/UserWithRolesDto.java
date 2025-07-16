package mate.academy.demo.dto.user;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithRolesDto {
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roleNames;
}
