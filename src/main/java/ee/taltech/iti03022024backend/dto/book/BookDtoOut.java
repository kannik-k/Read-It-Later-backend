package ee.taltech.iti03022024backend.dto.book;

import lombok.Data;

@Data
public class BookDtoOut {
    private long bookId;
    private String title;
    private String author;
    private Long genreId;
}
