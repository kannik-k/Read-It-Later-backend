package ee.taltech.iti03022024backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data transfer object for logging in")
public class LoginRequestDto {
    @NotNull
    @Size(min = 5, max = 15, message = "should be 5-15 characters long")
    @Schema(description = "User's username", example = "renser")
    private String username;
    @NotNull
    @Size(min = 8, max = 15, message = "should be 8-15 characters long")
    @Schema(description = "User's password", example = "2ssaPterceS")
    private String password;
}
