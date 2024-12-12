package ee.taltech.iti03022024backend.services.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import ee.taltech.iti03022024backend.entities.review.ReviewEntity;
import ee.taltech.iti03022024backend.exceptions.IncorrectInputException;
import ee.taltech.iti03022024backend.mappers.review.ReviewMapper;
import ee.taltech.iti03022024backend.mappers.review.ReviewMapperImpl;
import ee.taltech.iti03022024backend.repositories.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Spy
    private ReviewMapper reviewMapper = new ReviewMapperImpl();

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void createReview() {
        // given
        ReviewDtoIn review = ReviewDtoIn.builder().bookId(1L).review("Test review").build();
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setBookId(review.getBookId());
        reviewEntity.setReview(review.getReview());

        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);

        // when
        var result = reviewService.createReview(review);

        // then
        then(reviewRepository).should().save(any(ReviewEntity.class));
        then(reviewMapper).should().toEntity(any(ReviewDtoIn.class));
        then(reviewMapper).should().toDto(any(ReviewEntity.class));
        assertEquals(ReviewDtoIn.builder().bookId(1L).review("Test review").build(), result);
    }

    @Test
    void createReview_ReviewDtoNull_ThrowException() {
        assertThrows(IncorrectInputException.class, () -> reviewService.createReview(null));
    }

    @Test
    void createReview_ReviewDtoHasNoText_ThrowException() {
        ReviewDtoIn review = ReviewDtoIn.builder().bookId(1L).review("").build();
        assertThrows(IncorrectInputException.class, () -> reviewService.createReview(review));
    }

    @Test
    void createReview_ReviewIsNull_ThrowException() {
        ReviewDtoIn review = ReviewDtoIn.builder().bookId(2L).review(null).build();

        assertThrows(IncorrectInputException.class, () -> reviewService.createReview(review));
    }

    @Test
    void createReview_ReviewBookIdNull_ThrowException() {
        ReviewDtoIn review = ReviewDtoIn.builder().bookId(0).review("review").build();

        assertThrows(IncorrectInputException.class, () -> reviewService.createReview(review));
    }

    @Test void createReview_BookIdNull_ThrowException() {
        ReviewDtoIn review = ReviewDtoIn.builder().bookId(0L).review("Test review").build();
        assertThrows(IncorrectInputException.class, () -> reviewService.createReview(review));
    }


    @Test
    void getBookReviews() {
        // given
        ReviewDtoIn firstReview = ReviewDtoIn.builder().bookId(1L).review("Test review the first").build();
        ReviewEntity firstReviewEntity = new ReviewEntity();
        firstReviewEntity.setBookId(firstReview.getBookId());
        firstReviewEntity.setReview(firstReview.getReview());

        ReviewDtoIn secondReview = ReviewDtoIn.builder().bookId(1L).review("Test review the second").build();
        ReviewEntity secontReviewEntity = new ReviewEntity();
        secontReviewEntity.setBookId(secondReview.getBookId());
        secontReviewEntity.setReview(secondReview.getReview());

        List<ReviewEntity> reviewList = List.of(firstReviewEntity, secontReviewEntity);
        Page<ReviewEntity> reviewPage = new PageImpl<>(reviewList, PageRequest.of(0, 10), reviewList.size());

        when(reviewRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(reviewPage);

        // when
        var result = reviewService.getBookReviews(1L, 0, 10);

        // then
        then(reviewRepository).should().findAll(any(Specification.class), any(Pageable.class));
        then(reviewMapper).should().toDtoList(anyList());
        assertNotNull(result);
        assertEquals(2, result.getReviews().size());
        assertFalse(result.isHasNextPage());
        assertEquals(firstReview.getReview(), result.getReviews().get(0).getReview());
        assertEquals(secondReview.getReview(), result.getReviews().get(1).getReview());
    }

    @Test
    void getBookReviews_BookIdIsNull_ThrowException() {
        assertThrows(IncorrectInputException.class, () -> reviewService.getBookReviews(null, 0, 10));
    }

    @Test
    void getBookReviews_NegativePage_ThrowException() {
        assertThrows(IncorrectInputException.class, () -> reviewService.getBookReviews(1L, -1, 10));
    }

    @Test
    void getBookReviews_SizeLessThanZero_ThrowException() {
        assertThrows(IncorrectInputException.class, () -> reviewService.getBookReviews(1L, 0, -1));
    }

    @Test void getBookReviews_PageGreaterThanTotalPages_ReturnsEmptyResult() {
        List<ReviewEntity> reviewList = List.of();
        Page<ReviewEntity> reviewPage = new PageImpl<>(reviewList, PageRequest.of(2, 10), reviewList.size());

        when(reviewRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(reviewPage);


        var result = reviewService.getBookReviews(1L, 2, 10); then(reviewRepository).should().findAll(any(Specification.class), any(Pageable.class));

        then(reviewMapper).should().toDtoList(anyList());
        assertNotNull(result);
        assertTrue(result.getReviews().isEmpty());
        assertFalse(result.isHasNextPage());
    }
}
