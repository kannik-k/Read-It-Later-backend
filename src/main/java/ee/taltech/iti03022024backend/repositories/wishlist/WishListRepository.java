package ee.taltech.iti03022024backend.repositories.wishlist;

import ee.taltech.iti03022024backend.entities.wishList.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WishListRepository extends JpaRepository<WishListEntity, Long>, JpaSpecificationExecutor<WishListEntity> {

    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
