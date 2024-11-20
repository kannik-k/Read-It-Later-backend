package ee.taltech.iti03022024backend.controllers.book;

import ee.taltech.iti03022024backend.dto.book.BookDtoIn;
import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.services.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api")
@RestController
public class BookController {
    private final BookService bookService;

    //POST
    @PostMapping("public/book")
    public ResponseEntity<BookDtoOut> createBook(@Valid @RequestBody BookDtoIn bookDtoIn) {
        BookDtoOut book = bookService.createBook(bookDtoIn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    //GET by id
    @GetMapping("public/book/searchById/{id}")
    public ResponseEntity<BookDtoOut> getBookById(@PathVariable("id") long id) {
        BookDtoOut book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    //GET all (also used for search bar to search by author/title)
    @GetMapping("public/book")
    public ResponseEntity<List<BookDtoOut>> getBooks(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genreId", required = false) Long genreId
    ) {

        List<BookDtoOut> bookList = bookService.getBooks(author, title, genreId);
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }
}
