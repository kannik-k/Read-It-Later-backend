package ee.taltech.iti03022024backend.dto.book;

import lombok.Data;

@Data
public class BookDtoIn {
    private String title;
    private String author;
    private Long genreId;
}
