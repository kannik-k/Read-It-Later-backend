package ee.taltech.iti03022024backend.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for book. Server sends to client..")
public class BookDtoOut {
    @Schema(description = "Unique book id", example = "1")
    private long bookId;
    @Schema(description = "Book title", example = "A Game of Thrones")
    private String title;
    @Schema(description = "Book title", example = "George R. R. Martin")
    private String author;
    @Schema(description = "Book genre as text", example = "Fantasy")
    private String genre;
}
