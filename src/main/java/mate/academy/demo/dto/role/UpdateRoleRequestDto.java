package mate.academy.demo.dto.role;

import lombok.Getter;
import lombok.Setter;
import mate.academy.demo.model.RoleName;

@Getter
@Setter
public class UpdateRoleRequestDto {
    private RoleName roleName;
}
