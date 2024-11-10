package ee.taltech.iti03022024backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDtoIn { // Client sends to server
    @NotNull
    @Size(min = 5, max = 15, message = "Username should be 5-15 characters long.")
    private String username;
    @NotNull
    @Size(min = 8, max = 15, message = "Password should be 8-15 characters long.")
    private String password;
    @NotNull
    @NotBlank
    @Email
    private String email;
}
