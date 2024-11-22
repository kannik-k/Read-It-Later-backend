package ee.taltech.iti03022024backend.dto.userpreferences;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for User preference. Clint sends to server.")
public class UserPreferencesDtoIn {
    @Schema(description = "User id, that will show which user has added the genre.", example = "4")
    private Long userId;
    @Schema(description = "Genre given as number.", example = "3")
    private Long genreId;
}
