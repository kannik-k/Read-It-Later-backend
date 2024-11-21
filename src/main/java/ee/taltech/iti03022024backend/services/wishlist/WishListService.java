package ee.taltech.iti03022024backend.services.wishlist;

import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoOut;
import ee.taltech.iti03022024backend.entities.wishlist.WishListEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.mappers.wishlist.WishListMapper;
import ee.taltech.iti03022024backend.repositories.wishlist.WishListRepository;
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

    public WishListDtoOut addToWishList(WishListDtoIn wishListDtoIn) {
        if (wishListRepository.existsByUserIdAndBookId(wishListDtoIn.getUserId(), wishListDtoIn.getBookId())) {
            throw new NameAlreadyExistsException("User already has book in wish list.");
        }

        WishListEntity wishListEntity = wishListMapper.toEntity(wishListDtoIn);
        wishListRepository.save(wishListEntity);
        return wishListMapper.toDto(wishListEntity);
    }

    public List<WishListDtoOut> getUserBooks(Long userId) {
        Specification<WishListEntity> spec = Specification.where(WishListSpecifications.getByUserId(userId));
        List<WishListEntity> wishListEntityList = wishListRepository.findAll(spec);
        return wishListMapper.toDtoList(wishListEntityList);
    }

    public void deleteBookFromWishList(Long userId, Long bookId) {
        Specification<WishListEntity> spec = Specification.where(WishListSpecifications.getByUserIdAndBookId(userId, bookId));
        List<WishListEntity> wishListEntityList = wishListRepository.findAll(spec);
        wishListRepository.deleteAll(wishListEntityList);
    }
}
