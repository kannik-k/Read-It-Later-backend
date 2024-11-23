package ee.taltech.iti03022024backend.dto.userpreferences;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for user preferences. Server sends to client..")
public class UserPreferencesDtoOut {
    @Schema(description = "Unique id", example = "1")
    private Long id;
    @Schema(description = "User id, that will show which user has added the genre.", example = "4")
    private Long userId;
    @Schema(description = "Genre name, genre id will not be displayed as a number.", example = "Mystery")
    private String genreId;
}
