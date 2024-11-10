package ee.taltech.iti03022024backend.dto.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookDtoIn {
    @NotNull
    @Size(min = 2, max = 75, message = "must be 10-75 characters long")
    private String title;
    @NotNull
    @Size(min = 5, max = 30, message = "must be 5-30 characters long")
    private String author;
    @NotNull
    private Long genreId;
}
