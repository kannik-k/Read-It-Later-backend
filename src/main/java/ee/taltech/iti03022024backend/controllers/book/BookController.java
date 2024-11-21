package ee.taltech.iti03022024backend.controllers.book;

import ee.taltech.iti03022024backend.dto.book.BookDtoIn;
import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.dto.book.BookPageResponseDto;
import ee.taltech.iti03022024backend.services.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api")
@RestController
public class BookController {
    private final BookService bookService;

    //POST
    @PostMapping("book")
    public ResponseEntity<BookDtoOut> createBook(@Valid @RequestBody BookDtoIn bookDtoIn) {
        BookDtoOut book = bookService.createBook(bookDtoIn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    //GET by id
    @GetMapping("public/book/search_by_id/{id}")
    public ResponseEntity<BookDtoOut> getBookById(@PathVariable("id") long id) {
        BookDtoOut book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    //GET all (also used for search bar to search by author/title)
    @GetMapping("public/book")
    public ResponseEntity<BookPageResponseDto> getBooks(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genreId", required = false) Long genreId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        BookPageResponseDto bookList = bookService.getBooks(author, title, genreId, page, size);
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    @GetMapping("public/hello")
    public String hello() {
        return "HELLO";
    }
}
