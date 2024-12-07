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
import ee.taltech.iti03022024backend.services.genre.GenreService;
import ee.taltech.iti03022024backend.specifications.wishlist.WishListSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WishListService {

    private final WishListMapper wishListMapper;
    private final WishListRepository wishListRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreService genreService;

    public WishListDtoOut addToWishList(Long userId, WishListDtoIn wishListDtoIn) {
        if (wishListRepository.existsByUserIdAndBookId(userId, wishListDtoIn.getBookId())) {
            throw new NameAlreadyExistsException("User already has book in wish list.");
        }

        WishListEntity wishListEntity = wishListMapper.toEntity(wishListDtoIn);
        wishListEntity.setUserId(userId);
        wishListRepository.save(wishListEntity);
        return wishListMapper.toDto(wishListEntity);
    }

    public List<BookDtoOut> getUserBooks(Long userId) {
        Specification<WishListEntity> spec = Specification.where(WishListSpecifications.getByUserId(userId));
        List<WishListEntity> wishListEntityList = wishListRepository.findAll(spec);
        List<Long> booksById = wishListEntityList.stream().map(WishListEntity::getBookId).toList();
        List<BookEntity> books = bookRepository.findAllById(booksById);
        return books.stream().map(
                bookEntity -> {
                    BookDtoOut bookDtoOut = bookMapper.toDto(bookEntity);
                    String bookGenre = genreService.getGenreById(bookEntity.getGenreId());
                    bookDtoOut.setGenre(bookGenre);
                    return bookDtoOut;
                })
                .toList();
    }

    public void deleteBookFromWishList(Long userId, Long bookId) {
        Specification<WishListEntity> spec = Specification.where(WishListSpecifications.getByUserIdAndBookId(userId, bookId));
        List<WishListEntity> wishListEntityList = wishListRepository.findAll(spec);
        wishListRepository.deleteAll(wishListEntityList);
    }

    public void deleteByUserId(Long userId) {
        wishListRepository.deleteByUserId(userId);
    }
}
