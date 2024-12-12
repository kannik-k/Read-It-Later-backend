package ee.taltech.iti03022024backend.services.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import ee.taltech.iti03022024backend.entities.review.ReviewEntity;
import ee.taltech.iti03022024backend.exceptions.IncorrectInputException;
import ee.taltech.iti03022024backend.mappers.review.ReviewMapper;
import ee.taltech.iti03022024backend.repositories.review.ReviewRepository;
import ee.taltech.iti03022024backend.response.review.ReviewPageResponse;
import ee.taltech.iti03022024backend.specifications.review.ReviewSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


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

    public ReviewPageResponse getBookReviews(Long bookId, int page, int size) {
        if (bookId == null) {
            throw new IncorrectInputException("Book cannot be null");
        }

        if (page < 0 || size <= 0) {
            throw new IncorrectInputException("Page number cannot be less than zero");
        }

        Specification<ReviewEntity> spec = Specification.where(ReviewSpecifications.getByBookId(bookId));
        Pageable pageable = PageRequest.of(page, size);
        Slice<ReviewEntity> slice = reviewRepository.findAll(spec, pageable);

        boolean hasNextPage = slice.hasNext();
        return new ReviewPageResponse(reviewMapper.toDtoList(slice.getContent()), hasNextPage);
    }

}
