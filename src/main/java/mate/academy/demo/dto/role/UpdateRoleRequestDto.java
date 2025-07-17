package mate.academy.demo.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mate.academy.demo.model.RoleName;

@Getter
@Setter
public class UpdateRoleRequestDto {
    @NotNull(message = "Role name must not be null")
    private RoleName roleName;
}
