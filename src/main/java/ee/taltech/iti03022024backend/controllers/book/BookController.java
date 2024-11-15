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
@RequestMapping("api/book")
@RestController
public class BookController {
    private final BookService bookService;

    //POST
    @PostMapping()
    public ResponseEntity<BookDtoOut> createBook(@Valid @RequestBody BookDtoIn bookDtoIn) {
        BookDtoOut book = bookService.createBook(bookDtoIn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    //GET by id
    @GetMapping("searchById/{id}")
    public ResponseEntity<BookDtoOut> getBookById(@PathVariable("id") long id) {
        BookDtoOut book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    //GET all
    @GetMapping()
    public ResponseEntity<List<BookDtoOut>> getBooks() {
        List<BookDtoOut> bookList = bookService.getBooks();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }


    //GET by genre
    @GetMapping("searchByGenre/{genreId}")
    public ResponseEntity<List<BookDtoOut>> getBooksByGenre(@PathVariable("genreId") long genreId) {
        List<BookDtoOut> bookList = bookService.getBooksByGenre(genreId);
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }
}
