package ee.taltech.iti03022024backend.dto.userpreferences;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data transfer object for user preferences. Server sends to client..")
public class UserPreferencesDtoOut {
    @Schema(description = "Unique id", example = "1")
    private Long id;
    @Schema(description = "User id, that will show which user has added the genre.", example = "4")
    private Long userId;
    @Schema(description = "Genre id.", example = "1")
    private Long genreId;
    @Schema(description = "Genre name.", example = "Mystery")
    private String genre;
}
