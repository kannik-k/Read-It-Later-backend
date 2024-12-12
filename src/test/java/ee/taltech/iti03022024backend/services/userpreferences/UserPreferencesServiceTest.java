package ee.taltech.iti03022024backend.services.userpreferences;

import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import ee.taltech.iti03022024backend.entities.usepreferences.UserPreferencesEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.userpreferences.UserPreferencesMapper;
import ee.taltech.iti03022024backend.repositories.books.BookRepository;
import ee.taltech.iti03022024backend.repositories.userpreferences.UserPreferencesRepository;
import ee.taltech.iti03022024backend.response.book.BookPageResponse;
import ee.taltech.iti03022024backend.services.book.BookService;
import ee.taltech.iti03022024backend.services.genre.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserPreferencesServiceTest {
    @Mock
    private UserPreferencesRepository userPreferencesRepository;
    @Mock
    private GenreService genreService;
    @Mock
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Spy
    private UserPreferencesMapper userPreferencesMapper;

    @InjectMocks
    private UserPreferencesService userPreferencesService;


    @Test
    void addGenre_whenCorrect_returnsGenre() {
        // Given
        Long userId = 1L;
        UserPreferencesDtoIn inputDto = new UserPreferencesDtoIn();
        inputDto.setGenreId(10L);
        UserPreferencesEntity entity = new UserPreferencesEntity();
        entity.setUserId(userId);
        entity.setGenreId(10L);

        when(userPreferencesRepository.existsByUserIdAndGenreId(userId, 10L)).thenReturn(false);
        when(userPreferencesMapper.toEntity(inputDto)).thenReturn(entity);
        when(userPreferencesRepository.save(entity)).thenReturn(entity);
        when(userPreferencesMapper.toDto(entity)).thenReturn(UserPreferencesDtoOut.builder().userId(userId).genreId(10L).build());

        // When
        UserPreferencesDtoOut result = userPreferencesService.addGenre(inputDto, userId);

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getGenreId());
        verify(userPreferencesRepository, times(1)).save(entity);
    }

    @Test
    void addGenre_whenGenreAlreadyExists_throwsException() {
        // Given
        Long userId = 1L;
        UserPreferencesDtoIn inputDto = new UserPreferencesDtoIn();
        inputDto.setGenreId(10L);

        when(userPreferencesRepository.existsByUserIdAndGenreId(userId, 10L)).thenReturn(true);

        // When
        Throwable thrown = catchThrowable(() -> userPreferencesService.addGenre(inputDto, userId));

        //Then
        assertThat(thrown).isInstanceOf(NameAlreadyExistsException.class).hasMessage("Genre already exists.");
    }


    @Test
    void getGenres_whenCorrect_returnsGenres() {
        // Given
        Long userId = 1L;
        UserPreferencesEntity entity = new UserPreferencesEntity();
        entity.setUserId(userId);
        entity.setGenreId(10L);

        List<UserPreferencesEntity> entities = List.of(entity);
        when(userPreferencesRepository.findAll(any(Specification.class))).thenReturn(entities);
        when(userPreferencesMapper.toDto(entity)).thenReturn(UserPreferencesDtoOut.builder().userId(1L).genreId(10L).build());
        when(genreService.getGenreById(10L)).thenReturn("Fantasy");

        // When
        List<UserPreferencesDtoOut> result = userPreferencesService.getGenres(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fantasy", result.getFirst().getGenre());
    }

    @Test
    void getGenres_whenNoGenresAdded_returnsEmptyResponse() {
        // Given
        Long userId = 1L;

        List<UserPreferencesEntity> entities = List.of();
        when(userPreferencesRepository.findAll(any(Specification.class))).thenReturn(entities);

        // When
        List<UserPreferencesDtoOut> result = userPreferencesService.getGenres(userId);

        // Then
        then(userPreferencesMapper).shouldHaveNoInteractions();
        then(genreService).shouldHaveNoInteractions();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getRecommendedBooks_whenBooksFound_returnsBooks() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 2;

        UserPreferencesEntity preference1 = new UserPreferencesEntity();
        preference1.setUserId(userId);
        preference1.setGenreId(10L);

        UserPreferencesEntity preference2 = new UserPreferencesEntity();
        preference2.setUserId(userId);
        preference2.setGenreId(20L);

        List<UserPreferencesEntity> preferences = List.of(preference1, preference2);

        BookEntity book1 = new BookEntity();
        book1.setBookId(1);
        book1.setGenreId(10L);

        BookEntity book2 = new BookEntity();
        book2.setBookId(2);
        book2.setGenreId(20L);

        Slice<BookEntity> bookSlice = new SliceImpl<>(List.of(book1, book2), PageRequest.of(page, size), true);

        when(userPreferencesRepository.findAll(any(Specification.class))).thenReturn(preferences);
        when(bookRepository.findAllByGenreIdIn(anyList(), any(Pageable.class))).thenReturn(bookSlice);
        when(bookService.genreIdToGenre(List.of(book1, book2)))
                .thenReturn(List.of(
                        BookDtoOut.builder().bookId(1).genre("Fantasy").build(),
                        BookDtoOut.builder().bookId(2).genre("Sci-Fi").build()
                ));


        // When
        BookPageResponse response = userPreferencesService.getRecommendedBooks(userId, page, size);

        // Then
        assertNotNull(response);
        assertEquals(2, response.getBooks().size());
        assertTrue(response.isHasNextPage());
        assertEquals("Fantasy", response.getBooks().get(0).getGenre());
        assertEquals("Sci-Fi", response.getBooks().get(1).getGenre());

        // Verify interactions
        verify(bookService, times(1)).genreIdToGenre(anyList());
        verify(bookRepository, times(1)).findAllByGenreIdIn(anyList(), any(Pageable.class));
    }

    @Test
    void getRecommendedBooks_whenNoBooksFound_returnsEmptyResponse() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 2;

        UserPreferencesEntity preference = new UserPreferencesEntity();
        preference.setUserId(userId);
        preference.setGenreId(10L);

        List<UserPreferencesEntity> preferences = List.of(preference);

        Slice<BookEntity> bookSlice = new SliceImpl<>(List.of(), PageRequest.of(page, size), false);

        when(userPreferencesRepository.findAll(any(Specification.class))).thenReturn(preferences);
        when(bookRepository.findAllByGenreIdIn(anyList(), any(Pageable.class))).thenReturn(bookSlice);

        // When
        BookPageResponse response = userPreferencesService.getRecommendedBooks(userId, page, size);

        // Then
        assertNotNull(response);
        assertEquals(0, response.getBooks().size());
        assertFalse(response.isHasNextPage());
    }

    @Test
    void getRecommendedBooks_whenNoPreferencesFound_returnsEmptyResponse() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 2;

        UserPreferencesEntity preference = new UserPreferencesEntity();
        preference.setUserId(userId);
        preference.setGenreId(10L);

        List<UserPreferencesEntity> preferences = List.of(preference);

        Slice<BookEntity> emptySlice = new SliceImpl<>(List.of(), PageRequest.of(page, size), false);

        when(userPreferencesRepository.findAll(any(Specification.class))).thenReturn(preferences);
        when(bookRepository.findAllByGenreIdIn(anyList(), any(Pageable.class))).thenReturn(emptySlice);

        // When
        BookPageResponse response = userPreferencesService.getRecommendedBooks(userId, page, size);

        // Then
        assertNotNull(response);
        assertEquals(0, response.getBooks().size());
        assertFalse(response.isHasNextPage());
    }


    @Test
    void deleteGenre_whenCorrect_removesGenre() {
        // Given
        Long userId = 1L;
        String genreName = "Fantasy";
        Long genreId = 100L;

        UserPreferencesEntity entity = new UserPreferencesEntity();
        entity.setUserId(userId);
        entity.setGenreId(genreId);

        when(genreService.getGenreByName(genreName)).thenReturn(genreId);
        when(userPreferencesRepository.findByUserIdAndGenreId(userId, genreId)).thenReturn(List.of(entity));

        // When
        userPreferencesService.deleteGenre(userId, genreName);

        // Then

        verify(userPreferencesRepository, times(1)).deleteAll(List.of(entity));
    }

    @Test
    void deleteGenre_whenGenreNotFound_throwsException() {
        // Given
        Long userId = 1L;
        String genreName = "Unknown Genre";

        when(genreService.getGenreByName(genreName)).thenReturn(null);

        // When/Then
        assertThrows(NotFoundException.class, () -> userPreferencesService.deleteGenre(userId, genreName));
    }

    @Test
    void deleteGenre_whenUserHasNoGenres_throwsException() {
        // Given
        Long userId = 1L;
        String genreName = "Fantasy";
        Long genreId = 10L;

        when(genreService.getGenreByName(genreName)).thenReturn(genreId);
        // When
        Throwable thrown = catchThrowable(() -> userPreferencesService.deleteGenre(userId, genreName));

        // Then
        assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessage("No preferences found for user " + userId + " with genre " + genreName);

    }

    @Test
    void deleteByUserId() {
        // Given
        Long userId = 1L;

        // When
        userPreferencesService.deleteByUserId(userId);

        // Then
        verify(userPreferencesRepository, times(1)).deleteByUserId(userId);
    }
}