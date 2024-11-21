package ee.taltech.iti03022024backend.specifications.wishlist;

import ee.taltech.iti03022024backend.entities.wishlist.WishListEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class WishListSpecifications {

    private WishListSpecifications() {}

    public static Specification<WishListEntity> getByUserId(Long userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("userId"), userId);
    }

    public static Specification<WishListEntity> getByUserIdAndBookId(Long userId, Long bookId) {
        return (root, query, cb) -> {
            if (userId == null || bookId == null) return null;
            Predicate userPredicate = cb.equal(root.get("userId"), userId);
            Predicate bookPredicate = cb.equal(root.get("bookId"), bookId);
            return cb.and(userPredicate, bookPredicate);
        };
    }
}
