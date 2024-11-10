package ee.taltech.iti03022024backend.repositories.review;

import ee.taltech.iti03022024backend.entities.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
