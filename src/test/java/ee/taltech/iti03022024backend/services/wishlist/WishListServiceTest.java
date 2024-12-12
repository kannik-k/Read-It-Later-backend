package ee.taltech.iti03022024backend.services.wishlist;

import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import ee.taltech.iti03022024backend.entities.wishlist.WishListEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.mappers.book.BookMapper;
import ee.taltech.iti03022024backend.mappers.wishlist.WishListMapper;
import ee.taltech.iti03022024backend.repositories.books.BookRepository;
import ee.taltech.iti03022024backend.repositories.wishlist.WishListRepository;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class WishListServiceTest {
@Mock
private WishListRepository wishListRepository;
@Mock
private BookRepository bookRepository;
@Mock
private GenreService genreService;
@Spy
private WishListMapper wishListMapper;

@Spy
private BookMapper bookMapper;

@InjectMocks
private WishListService wishListService;

    @Test
    void addToWishList_whenCorrect_returnsWishListDto() {
        // Given
        Long userId = 1L;
        Long bookId = 100L;
        WishListDtoIn wishListDtoIn = new WishListDtoIn();
        wishListDtoIn.setBookId(bookId);

        WishListEntity entity = new WishListEntity();
        entity.setUserId(userId);
        entity.setBookId(bookId);

        when(wishListRepository.existsByUserIdAndBookId(userId, bookId)).thenReturn(false);
        when(wishListMapper.toEntity(wishListDtoIn)).thenReturn(entity);
        when(wishListRepository.save(entity)).thenReturn(entity);
        when(wishListMapper.toDto(entity)).thenReturn(WishListDtoOut.builder().userId(userId).bookId(bookId).build());

        // When
        WishListDtoOut result = wishListService.addToWishList(userId, wishListDtoIn);

        // Then
        then(wishListMapper).should().toEntity(wishListDtoIn);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(bookId, result.getBookId());
        verify(wishListRepository, times(1)).save(entity);
    }

    @Test
    void addToWishList_whenAlreadyExists_throwsException() {
        // Given
        Long userId = 1L;
        Long bookId = 100L;
        WishListDtoIn wishListDtoIn = new WishListDtoIn();
        wishListDtoIn.setBookId(bookId);

        when(wishListRepository.existsByUserIdAndBookId(userId, bookId)).thenReturn(true);

        Throwable thrown = catchThrowable(() -> wishListService.addToWishList(userId, wishListDtoIn));
        // When/Then

        assertThat(thrown).isInstanceOf(NameAlreadyExistsException.class).hasMessage("User already has book in wish list.");
        then(wishListMapper).shouldHaveNoInteractions();
    }

    @Test
    void getUserBooks_whenCorrect_returnsBooks() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 2;

        WishListEntity wishListEntity = new WishListEntity();
        wishListEntity.setUserId(userId);
        wishListEntity.setBookId(100L);

        Page<WishListEntity> wishListPage = new PageImpl<>(
                List.of(wishListEntity),
                PageRequest.of(page, size),
                1
        );

        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookId(100);
        bookEntity.setGenreId(10L);
        bookEntity.setTitle("Book Title");
        bookEntity.setAuthor("Book Author");

        when(wishListRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(wishListPage);
        when(bookRepository.findAllById(List.of(100L))).thenReturn(List.of(bookEntity));
        when(genreService.getGenreById(10L)).thenReturn("Fiction");
        when(bookMapper.toDto(bookEntity)).thenReturn(
                BookDtoOut.builder()
                        .bookId(100)
                        .title("Book Title")
                        .author("Book Author")
                        .genre("Fiction")
                        .build()
        );

        // When
        BookPageResponse response = wishListService.getUserBooks(userId, page, size);

        // Then
        then(bookRepository).should().findAllById(List.of(wishListEntity.getBookId()));
        then(bookMapper).should().toDto(bookEntity);
        then(genreService).should().getGenreById(bookEntity.getGenreId());
        assertNotNull(response);
        assertEquals(1, response.getBooks().size());
        assertEquals("Fiction", response.getBooks().getFirst().getGenre());
        assertFalse(response.isHasNextPage());
    }

    @Test
    void getUserBooks_whenNoBooksAddedYet_returnsEmptyResponse() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 2;

        Page<WishListEntity> wishListPage = new PageImpl<>(
                List.of(),
                PageRequest.of(page, size),
                1
        );

        when(wishListRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(wishListPage);

        // When
        BookPageResponse response = wishListService.getUserBooks(userId, page, size);

        // Then
        then(bookMapper).shouldHaveNoInteractions();
        then(genreService).shouldHaveNoInteractions();
        assertNotNull(response);
        assertEquals(0, response.getBooks().size());
        assertFalse(response.isHasNextPage());
    }

    @Test
    void deleteBookFromWishList() {
        // Given
        Long userId = 1L;
        Long bookId = 100L;

        WishListEntity wishListEntity = new WishListEntity();
        wishListEntity.setUserId(userId);
        wishListEntity.setBookId(bookId);

        when(wishListRepository.findAll((Specification<WishListEntity>) any())).thenReturn(List.of(wishListEntity));

        // When
        wishListService.deleteBookFromWishList(userId, bookId);

        // Then
        verify(wishListRepository, times(1)).deleteAll(List.of(wishListEntity));
    }

    @Test
    void deleteByUserId() {
        // Given
        Long userId = 1L;

        // When
        wishListService.deleteByUserId(userId);

        // Then
        verify(wishListRepository, times(1)).deleteByUserId(userId);
    }
}
