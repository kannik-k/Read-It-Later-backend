package ee.taltech.iti03022024backend.controllers.book;

import ee.taltech.iti03022024backend.dto.book.BookDtoIn;
import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.response.book.BookPageResponse;
import ee.taltech.iti03022024backend.services.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api")
@RestController
@Tag(name = "Books", description = "API for managing books.")
public class BookController {
    private final BookService bookService;

    @Operation(
            summary = "Add new book to database.",
            description = "Creates a new book object based on given values."
    )
    @ApiResponse(responseCode = "200", description = "Book is added to the database successfully.")
    @PostMapping("book")
    public ResponseEntity<BookDtoOut> createBook(@Valid @RequestBody BookDtoIn bookDtoIn) {
        BookDtoOut book = bookService.createBook(bookDtoIn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @Operation(
            summary = "Get book from database based on book id",
            description = "Retrieves a book from databases based on its id."
    )
    @ApiResponse(responseCode = "200", description = "Book has been retrieved from database successfully.")
    @GetMapping("public/book/search_by_id/{id}")
    public ResponseEntity<BookDtoOut> getBookById(@PathVariable("id") long id) {
        BookDtoOut book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieves a list of books from database based on given arguments.",
            description = "Retrieves books by genre, title, or author. Returns all books (10 per page) if no filters are applied."
    )
    @ApiResponse(responseCode = "200", description = "List of suitable books has been retrieved successfully.")
    @GetMapping("public/book")
    public ResponseEntity<BookPageResponse> getBooks(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genreId", required = false) Long genreId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        BookPageResponse bookList = bookService.getBooks(author, title, genreId, page, size);
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }
}
