package ee.taltech.iti03022024backend.services.book;

import ee.taltech.iti03022024backend.dto.book.BookDtoIn;
import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.exceptions.UnfilledFieldException;
import ee.taltech.iti03022024backend.mappers.book.BookMapper;
import ee.taltech.iti03022024backend.repositories.books.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDtoOut createBook(BookDtoIn bookDtoIn) {
        if(bookDtoIn.getTitle() == null || bookDtoIn.getAuthor() == null || bookDtoIn.getGenreId() == null) {
            throw new UnfilledFieldException("Please fill out all fields");
        }
        BookEntity bookEntity = bookMapper.toEntity(bookDtoIn);
        bookRepository.save(bookEntity);
        return bookMapper.toDto(bookEntity);
    }

    public BookDtoOut getBookById(Long id) throws NotFoundException{
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        return bookMapper.toDto(bookEntity);
    }

    public List<BookDtoOut> getBooks() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        return bookMapper.toDtoList(bookEntities);
    }

    public List<BookDtoOut> getBooksByGenre(Long genreId) {
        List<BookEntity> bookEntities = bookRepository.findAll().stream()
                .filter(book -> genreId.equals(book.getGenreId()))
                .sorted(Comparator.comparing(BookEntity::getTitle))
                .collect(Collectors.toList());
        return bookMapper.toDtoList(bookEntities);
    }

    public void deleteBook(long id) throws NotFoundException {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Cannot delete a book that does not exist"));
        bookRepository.delete(bookEntity);
    }
}
