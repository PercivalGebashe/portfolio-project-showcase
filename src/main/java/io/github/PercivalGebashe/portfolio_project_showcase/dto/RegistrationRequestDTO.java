package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {

    private String firstName;
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(
            regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid email format."
    )
    private String emailAddress;

    @NotBlank(message = "Password is required.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain one uppercase, one lowercase, one number, and one special character"
    )
    private String password;
}