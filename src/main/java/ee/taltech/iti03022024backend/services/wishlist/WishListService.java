package ee.taltech.iti03022024backend.services.wishlist;

import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import ee.taltech.iti03022024backend.entities.wishlist.WishListEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.mappers.wishlist.WishListMapper;
import ee.taltech.iti03022024backend.repositories.books.BookRepository;
import ee.taltech.iti03022024backend.repositories.wishlist.WishListRepository;
import ee.taltech.iti03022024backend.response.book.BookPageResponse;
import ee.taltech.iti03022024backend.services.book.BookService;
import ee.taltech.iti03022024backend.specifications.wishlist.WishListSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WishListService {

    private final WishListMapper wishListMapper;
    private final WishListRepository wishListRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    public WishListDtoOut addToWishList(Long userId, WishListDtoIn wishListDtoIn) {
        if (wishListRepository.existsByUserIdAndBookId(userId, wishListDtoIn.getBookId())) {
            throw new NameAlreadyExistsException("User already has book in wish list.");
        }

        WishListEntity wishListEntity = wishListMapper.toEntity(wishListDtoIn);
        wishListEntity.setUserId(userId);
        wishListRepository.save(wishListEntity);
        return wishListMapper.toDto(wishListEntity);
    }

    public BookPageResponse getUserBooks(Long userId, int page, int size) {
        Specification<WishListEntity> spec = Specification.where(WishListSpecifications.getByUserId(userId));

        Pageable pageable = PageRequest.of(page, size);

        Slice<WishListEntity> slice = wishListRepository.findAll(spec, pageable);

        boolean isLastPage = slice.isLast();

        List<Long> booksById = slice.stream().map(WishListEntity::getBookId).toList();
        List<BookEntity> books = bookRepository.findAllById(booksById);
        List<BookDtoOut> booksList = bookService.genreIdToGenre(books);
        return new BookPageResponse(booksList, !isLastPage);
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
