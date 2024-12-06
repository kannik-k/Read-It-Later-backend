package ee.taltech.iti03022024backend.services.wishlist;

import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoOut;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import ee.taltech.iti03022024backend.entities.wishlist.WishListEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.wishlist.WishListMapper;
import ee.taltech.iti03022024backend.repositories.user.UserRepository;
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
    private final UserRepository userRepository;

    private static final String USER_NONEXISTENT = "User does not exist";

    public WishListDtoOut addToWishList(String userName, WishListDtoIn wishListDtoIn) {
        long userId = userRepository.findByUsername(userName).orElseThrow(() -> new NotFoundException(USER_NONEXISTENT)).getUserId();
        if (wishListRepository.existsByUserIdAndBookId(userId, wishListDtoIn.getBookId())) {
            throw new NameAlreadyExistsException("User already has book in wish list.");
        }

        WishListEntity wishListEntity = wishListMapper.toEntity(wishListDtoIn);
        wishListEntity.setUserId(userId);
        wishListRepository.save(wishListEntity);
        return wishListMapper.toDto(wishListEntity);
    }

    public List<WishListDtoOut> getUserBooks(String userName) {
        UserEntity user = userRepository.findByUsername(userName).orElseThrow(() -> new NotFoundException(USER_NONEXISTENT));
        Specification<WishListEntity> spec = Specification.where(WishListSpecifications.getByUserId(user.getUserId()));
        List<WishListEntity> wishListEntityList = wishListRepository.findAll(spec);
        return wishListMapper.toDtoList(wishListEntityList);
    }

    public void deleteBookFromWishList(String userName, Long bookId) {
        Specification<WishListEntity> spec = Specification.where(WishListSpecifications.getByUserIdAndBookId(userRepository.findByUsername(userName).orElseThrow(() -> new NotFoundException(USER_NONEXISTENT)).getUserId(), bookId));
        List<WishListEntity> wishListEntityList = wishListRepository.findAll(spec);
        wishListRepository.deleteAll(wishListEntityList);
    }
}
