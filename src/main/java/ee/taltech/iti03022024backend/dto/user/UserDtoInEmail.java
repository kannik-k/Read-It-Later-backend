package ee.taltech.iti03022024backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data transfer object for updating email")
public class UserDtoInEmail { // For updating email
    @NotNull
    @NotBlank
    @Email
    @Schema(description = "New user's email", example = "speter@gmail.com")
    private String email;
}
