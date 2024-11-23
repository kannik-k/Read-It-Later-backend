package ee.taltech.iti03022024backend.response.book;

import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BookPageResponse {
    private List<BookDtoOut> books;
    private boolean hasNextPage;

    public BookPageResponse(List<BookDtoOut> books, boolean hasNextPage) {
        this.books = books;
        this.hasNextPage = hasNextPage;
    }
}
