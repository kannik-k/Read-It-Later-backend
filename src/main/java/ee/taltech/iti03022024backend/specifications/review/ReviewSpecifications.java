package ee.taltech.iti03022024backend.specifications.review;

import ee.taltech.iti03022024backend.entities.review.ReviewEntity;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecifications {

    private ReviewSpecifications() {}

    public static Specification<ReviewEntity> getByBookId(Long bookId) {
        return (root, query, cb) ->
                bookId == null ? null : cb.equal(root.get("bookId"), bookId);
    }
}
