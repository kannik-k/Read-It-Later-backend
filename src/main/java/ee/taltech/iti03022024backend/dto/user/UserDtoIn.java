package ee.taltech.iti03022024backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for user. Clint sends to server.")
public class UserDtoIn { // Client sends to server
    @NotNull
    @Size(min = 5, max = 15, message = "should be 5-15 characters long")
    @Schema(description = "User's username", example = "renser")
    private String username;
    @NotNull
    @Size(min = 8, max = 15, message = "should be 8-15 characters long")
    @Schema(description = "User's password", example = "SecretPass1")
    private String password;
    @NotNull
    @Size(min = 8, max = 15, message = "should be 8-15 characters long")
    @Schema(description = "Repeat user's password", example = "SecretPass1")
    private String passwordAgain;
    @NotNull
    @NotBlank
    @Email
    @Schema(description = "User's email", example = "lirenser@gmail.com")
    private String email;
}
