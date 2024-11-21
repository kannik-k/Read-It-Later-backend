package ee.taltech.iti03022024backend.dto.book;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BookPageResponseDto {
    private List<BookDtoOut> books;
    private boolean hasNextPage;

    public BookPageResponseDto(List<BookDtoOut> books, boolean hasNextPage) {
        this.books = books;
        this.hasNextPage = hasNextPage;
    }
}
