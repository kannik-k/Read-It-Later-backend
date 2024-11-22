package ee.taltech.iti03022024backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Password change wrapper for user")
public class UserPasswordChangeWrapper {
    @NotNull
    @NotBlank
    @Schema(description = "User's old password", example = "SecretPass1")
    private String oldPassword;
    @NotNull
    @Size(min = 8, max = 15, message = "Password should be 8-15 characters long.")
    @Schema(description = "User's new password", example = "2ssaPterceS")
    private String newPassword;
    @NotNull
    @Size(min = 8, max = 15, message = "Password should be 8-15 characters long.")
    @Schema(description = "Repeat user's new password", example = "2ssaPterceS")
    private String confirmNewPassword;
}
