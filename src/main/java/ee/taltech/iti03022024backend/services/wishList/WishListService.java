package ee.taltech.iti03022024backend.services.wishList;

import ee.taltech.iti03022024backend.dto.wishList.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishList.WishListDtoOut;
import ee.taltech.iti03022024backend.entities.wishList.WishListEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.mappers.wishList.WishListMapper;
import ee.taltech.iti03022024backend.repositories.wishList.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WishListService {

    private final WishListMapper wishListMapper;
    private final WishListRepository wishListRepository;

    public WishListDtoOut addToWishList(WishListDtoIn wishListDtoIn) {
        if (wishListRepository.existsByUserIdAndBookId(wishListDtoIn.getUserId(), wishListDtoIn.getBookId())) {
            throw new NameAlreadyExistsException("User already has book in wish list.");
        }

        WishListEntity wishListEntity = wishListMapper.toEntity(wishListDtoIn);
        wishListRepository.save(wishListEntity);
        return wishListMapper.toDto(wishListEntity);
    }

    public List<WishListDtoOut> getUserBooks(Long userId) {
        List<WishListEntity> wishListEntities = wishListRepository.findAll().stream()
                .filter(book -> userId.equals(book.getUserId()))
                .collect(Collectors.toList());
        return wishListMapper.toDtoList(wishListEntities);
    }

    public void deleteBookFromWishList(Long userId, Long bookId) {
        List<WishListEntity> wishListEntities = wishListRepository.findAll().stream()
                .filter(wishList -> wishList.getUserId().equals(userId) && wishList.getBookId().equals(bookId))
                .collect(Collectors.toList());

        wishListRepository.deleteAll(wishListEntities);
    }
}
