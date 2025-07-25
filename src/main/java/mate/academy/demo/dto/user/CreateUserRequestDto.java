package mate.academy.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mate.academy.demo.annotation.FieldMatch;

@FieldMatch(
        first = "password",
        second = "repeatPassword",
        message = "Passwords do not match"
)
@Getter
@Setter
@EqualsAndHashCode
public class CreateUserRequestDto {
    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Please repeat the password")
    private String repeatPassword;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
}
