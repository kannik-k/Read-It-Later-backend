package ee.taltech.iti03022024backend.services.book;

import ee.taltech.iti03022024backend.dto.book.BookDtoIn;
import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.book.BookMapper;
import ee.taltech.iti03022024backend.mappers.book.BookMapperImpl;
import ee.taltech.iti03022024backend.repositories.books.BookRepository;
import ee.taltech.iti03022024backend.response.book.BookPageResponse;
import ee.taltech.iti03022024backend.services.genre.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreService genreService;

    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @InjectMocks
    private BookService bookService;


    @Test
    void createBook() {
        BookDtoIn bookDtoIn = BookDtoIn.builder().title("book").author("human").genreId(1L).build();
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(bookDtoIn.getTitle());
        bookEntity.setAuthor(bookDtoIn.getAuthor());
        bookEntity.setGenreId(bookDtoIn.getGenreId());
        BookDtoOut expectedBookDtoOut = BookDtoOut.builder().title("book").author("human").genre("Fiction").build();

        when(bookMapper.toEntity(bookDtoIn)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);

        when(bookMapper.toDto(bookEntity)).thenReturn(expectedBookDtoOut);

        BookDtoOut result = bookService.createBook(bookDtoIn);

        then(bookMapper).should().toEntity(bookDtoIn);
        then(bookRepository).should().save(bookEntity);
        then(bookMapper).should().toDto(bookEntity);

        assertEquals(expectedBookDtoOut, result);
    }


    @Test
    void getBookById_ReturnBookDtoOut_WhenBookExists() throws NotFoundException {
        // Eeldame, et leiate raamatu
        Long bookId = 2L;
        BookEntity bookEntity = new BookEntity();
        bookEntity.setGenreId(bookId);
        bookEntity.setTitle("title");
        bookEntity.setAuthor("author");
        bookEntity.setGenreId(2L);

        BookDtoOut bookDtoOut = BookDtoOut.builder().bookId(bookId).title("title").author("author").genre("Horror").build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toDto(bookEntity)).thenReturn(bookDtoOut);
        when(genreService.getGenreById(bookEntity.getGenreId())).thenReturn("Horror");

        BookDtoOut result = bookService.getBookById(bookId);

        then(bookRepository).should().findById(bookId);
        then(bookMapper).should().toDto(bookEntity);

        assertEquals(bookDtoOut, result);
    }



    @Test
    void getBookById_ThrowNotFoundException_WhenBookDoesNotExist() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.getBookById(bookId));

        then(bookRepository).should().findById(bookId);
        then(bookMapper).shouldHaveNoInteractions();
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void genreIdToGenre_ReturnsEmptyList_WhenNoBooks() {
        List<BookEntity> emptyList = new ArrayList<>();
        List<BookDtoOut> result = bookService.genreIdToGenre(emptyList);
        assertTrue(result.isEmpty());
    }

    @Test
    void genreIdToGenre() {
        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setBookId(1);
        bookEntity1.setTitle("Book 1");
        bookEntity1.setAuthor("Author 1");
        bookEntity1.setGenreId(1L);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setBookId(2);
        bookEntity2.setTitle("Book 2");
        bookEntity2.setAuthor("Author 2");
        bookEntity2.setGenreId(2L);

        BookDtoOut bookDtoOut1 = BookDtoOut.builder().bookId(1L).title("Book 1").author("Author 1").genre("Horror").build();
        BookDtoOut bookDtoOut2 = BookDtoOut.builder().bookId(2L).title("Book 2").author("Author 2").genre("Thriller").build();

        when(bookMapper.toDto(bookEntity1)).thenReturn(bookDtoOut1);
        when(bookMapper.toDto(bookEntity2)).thenReturn(bookDtoOut2);

        when(genreService.getGenreById(1L)).thenReturn("Horror");
        when(genreService.getGenreById(2L)).thenReturn("Thriller");

        List<BookEntity> bookEntities = Arrays.asList(bookEntity1, bookEntity2);

        List<BookDtoOut> result = bookService.genreIdToGenre(bookEntities);

        assertEquals(2, result.size());
        assertEquals("Horror", result.get(0).getGenre());
        assertEquals("Thriller", result.get(1).getGenre());

        verify(genreService, times(1)).getGenreById(1L);
        verify(genreService, times(1)).getGenreById(2L);
        verify(bookMapper, times(1)).toDto(bookEntity1);
        verify(bookMapper, times(1)).toDto(bookEntity2);
    }

    @Test
    void testGetBooks_success() {
        String author = "Author Name";
        String title = "Book Title";
        Long genreId = 1L;
        int page = 0;
        int size = 10;
        String sort = "title-asc";

        BookEntity bookEntity = new BookEntity();
        bookEntity.setAuthor(author);
        bookEntity.setTitle(title);
        bookEntity.setGenreId(genreId);

        List<BookEntity> bookEntities = List.of(bookEntity);
        Page<BookEntity> bookPage = new PageImpl<>(bookEntities);

        BookDtoOut bookDtoOut = BookDtoOut.builder().bookId(1L).author(author).title(title).genre("Genre Name").build();

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDto(any(BookEntity.class))).thenReturn(bookDtoOut);
        when(genreService.getGenreById(genreId)).thenReturn("Genre Name");

        BookPageResponse result = bookService.getBooks(author, title, genreId, page, size, sort);

        verify(bookRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(bookMapper).toDto(any(BookEntity.class));
        verify(genreService).getGenreById(genreId);

        assertNotNull(result);
        assertEquals(1, result.getBooks().size());
        assertEquals("Genre Name", result.getBooks().get(0).getGenre());
        assertFalse(result.isHasNextPage());
    }

    @Test
    void testGetBooks_sortByTitleAsc() {
        String author = "Author Name";
        String title = "Book Title";
        Long genreId = 1L;
        int page = 0;
        int size = 10;
        String sort = "title-desc";

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setAuthor(author);
        bookEntity1.setTitle("Book Title A");
        bookEntity1.setGenreId(genreId);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setAuthor(author);
        bookEntity2.setTitle("Book Title Z");
        bookEntity2.setGenreId(genreId);

        List<BookEntity> bookEntities = List.of(bookEntity1, bookEntity2);
        Page<BookEntity> bookPage = new PageImpl<>(bookEntities);

        BookDtoOut bookDtoOut1 = BookDtoOut.builder().bookId(1L).author(author).title("Book Title A").genre("Genre Name").build();
        BookDtoOut bookDtoOut2 = BookDtoOut.builder().bookId(2L).author(author).title("Book Title Z").genre("Genre Name").build();

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDto(bookEntity1)).thenReturn(bookDtoOut1);
        when(bookMapper.toDto(bookEntity2)).thenReturn(bookDtoOut2);
        when(genreService.getGenreById(genreId)).thenReturn("Genre Name");

        BookPageResponse result = bookService.getBooks(author, title, genreId, page, size, sort);

        verify(bookRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(bookMapper).toDto(bookEntity1);
        verify(bookMapper).toDto(bookEntity2);
        verify(genreService, times(2)).getGenreById(genreId);

        assertNotNull(result);
        assertEquals(2, result.getBooks().size());
        assertEquals("Genre Name", result.getBooks().get(0).getGenre());
        assertEquals("Book Title A", result.getBooks().get(0).getTitle());
        assertEquals("Book Title Z", result.getBooks().get(1).getTitle());

        assertFalse(result.isHasNextPage());
    }

    @Test
    void testGetBooks_sortByAuthorAsc() {
        String author1 = "Author A";
        String author2 = "Author Z";
        Long genreId = 1L;
        int page = 0;
        int size = 10;
        String sort = "author-asc";

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setAuthor(author1);
        bookEntity1.setTitle("Book by Author A");
        bookEntity1.setGenreId(genreId);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setAuthor(author2);
        bookEntity2.setTitle("Book by Author Z");
        bookEntity2.setGenreId(genreId);

        List<BookEntity> bookEntities = List.of(bookEntity1, bookEntity2);
        Page<BookEntity> bookPage = new PageImpl<>(bookEntities);

        BookDtoOut bookDtoOut1 = BookDtoOut.builder().bookId(1L).author(author1).title("Book by Author A").genre("Genre Name").build();
        BookDtoOut bookDtoOut2 = BookDtoOut.builder().bookId(2L).author(author2).title("Book by Author Z").genre("Genre Name").build();

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDto(bookEntity1)).thenReturn(bookDtoOut1);
        when(bookMapper.toDto(bookEntity2)).thenReturn(bookDtoOut2);
        when(genreService.getGenreById(genreId)).thenReturn("Genre Name");

        BookPageResponse result = bookService.getBooks("", "", genreId, page, size, sort);

        verify(bookRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(bookMapper).toDto(bookEntity1);
        verify(bookMapper).toDto(bookEntity2);
        verify(genreService, times(2)).getGenreById(genreId);

        assertNotNull(result);
        assertEquals(2, result.getBooks().size());
        assertEquals("Genre Name", result.getBooks().get(0).getGenre());
        assertEquals("Book by Author A", result.getBooks().get(0).getTitle());
        assertEquals("Book by Author Z", result.getBooks().get(1).getTitle());

        assertFalse(result.isHasNextPage());
    }

}