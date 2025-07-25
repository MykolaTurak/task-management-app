package mate.academy.demo.dto.user;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserWithRolesDto {
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roleNames;
}
