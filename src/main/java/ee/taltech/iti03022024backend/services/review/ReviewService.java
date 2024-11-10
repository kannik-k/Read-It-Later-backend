package ee.taltech.iti03022024backend.services.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import ee.taltech.iti03022024backend.entities.review.ReviewEntity;
import ee.taltech.iti03022024backend.mappers.review.ReviewMapper;
import ee.taltech.iti03022024backend.repositories.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    public ReviewDtoIn createReview(ReviewDtoIn reviewDtoIn) {
        ReviewEntity reviewEntity = reviewMapper.toEntity(reviewDtoIn);
        reviewRepository.save(reviewEntity);
        return reviewMapper.toDto(reviewEntity);
    }

    public List<ReviewDtoIn> getBookReviews(Long bookId) {
        List<ReviewEntity> reviews = reviewRepository.findAll().stream()
                .filter(review -> review.getBookId().equals(bookId)).collect(Collectors.toList());
        return reviewMapper.toDtoList(reviews);
    }
}
