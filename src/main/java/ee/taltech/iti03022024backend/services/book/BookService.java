package ee.taltech.iti03022024backend.services.book;

import ee.taltech.iti03022024backend.controllers.genre.GenreController;
import ee.taltech.iti03022024backend.dto.book.BookDtoIn;
import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.book.BookMapper;
import ee.taltech.iti03022024backend.repositories.books.BookRepository;
import ee.taltech.iti03022024backend.specifications.book.BookSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreController genreController;

    public BookDtoOut createBook(BookDtoIn bookDtoIn) {
        BookEntity bookEntity = bookMapper.toEntity(bookDtoIn);
        bookRepository.save(bookEntity);
        String bookGenre = genreController.getGenreById(bookEntity.getGenreId()).getBody();
        BookDtoOut bookDtoOut = bookMapper.toDto(bookEntity);
        bookDtoOut.setGenre(bookGenre);

        return bookDtoOut;
    }

    public BookDtoOut getBookById(Long id) throws NotFoundException{
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        String bookGenre = genreController.getGenreById(bookEntity.getGenreId()).getBody();

        BookDtoOut bookDtoOut = bookMapper.toDto(bookEntity);
        bookDtoOut.setGenre(bookGenre);

        return bookDtoOut;
    }

    public List<BookDtoOut> getBooks(String author, String title, Long genreId) {
        Specification<BookEntity> specification = Specification
                .where(BookSpecifications.getByAuthor(author))
                .and(BookSpecifications.getByTitle(title))
                .and(BookSpecifications.getByGenreId(genreId));

        List<BookEntity> bookEntities = bookRepository.findAll(specification);

        return bookEntities.stream()
                .map(bookEntity -> {
                    BookDtoOut bookDtoOut = bookMapper.toDto(bookEntity);

                    String bookGenre = genreController.getGenreById(bookEntity.getGenreId()).getBody();
                    bookDtoOut.setGenre(bookGenre);

                    return bookDtoOut;
                })
                .toList();
    }
}
