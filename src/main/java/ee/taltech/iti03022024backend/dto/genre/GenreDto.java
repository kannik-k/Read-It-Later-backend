package ee.taltech.iti03022024backend.dto.genre;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for genre.")
public class GenreDto {
    @Schema(description = "Genre unique id", example = "1")
    private long genreId;
    @Schema(description = "Name of the genre", example = "Fiction")
    private String genre;
}
