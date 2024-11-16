package ee.taltech.iti03022024backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordChangeWrapper {
    @NotNull
    @NotBlank
    private String oldPassword;
    @NotNull
    @Size(min = 8, max = 15, message = "Password should be 8-15 characters long.")
    private String newPassword;
    @NotNull
    @Size(min = 8, max = 15, message = "Password should be 8-15 characters long.")
    private String confirmNewPassword;
}
