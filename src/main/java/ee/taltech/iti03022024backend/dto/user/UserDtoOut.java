package ee.taltech.iti03022024backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for user. Server sends to client.")
public class UserDtoOut {   // Server sends to client
    @Schema(description = "Unique user id", example = "1")
    private long userId;
    @Schema(description = "User's username", example = "renser")
    private String username;
    @Schema(description = "User's email", example = "lirenser@gmail.com")
    private String email;
}
