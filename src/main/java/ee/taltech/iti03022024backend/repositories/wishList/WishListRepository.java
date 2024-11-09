package ee.taltech.iti03022024backend.repositories.wishList;

import ee.taltech.iti03022024backend.entities.wishList.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
}
