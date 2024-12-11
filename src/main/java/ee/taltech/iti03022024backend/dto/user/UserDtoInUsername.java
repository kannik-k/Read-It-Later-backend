package ee.taltech.iti03022024backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data transfer object for changing username")
public class UserDtoInUsername {
    @NotNull
    @Size(min = 5, max = 15, message = "should be 5-15 characters long")
    @Schema(description = "New user's username", example = "Petersell")
    private String username;
}
