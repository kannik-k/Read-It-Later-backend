package ee.taltech.iti03022024backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDtoInEmail { // For updating email
    @NotNull
    @NotBlank
    @Email
    private String email;
}
