package ee.taltech.iti03022024backend.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for book. Clint sends to server.")
public class BookDtoIn {
    @NotNull
    @Size(min = 2, max = 75, message = "must be 10-75 characters long")
    @Schema(description = "Book title", example = "The Great Hunt")
    private String title;
    @NotNull
    @Size(min = 5, max = 30, message = "must be 5-30 characters long")
    @Schema(description = "Book author", example = "Robert Jordan")
    private String author;
    @NotNull
    @Schema(description = "Book genre, given as number", example = "2")
    private Long genreId;
}
