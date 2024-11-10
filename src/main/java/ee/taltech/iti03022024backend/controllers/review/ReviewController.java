package ee.taltech.iti03022024backend.controllers.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import ee.taltech.iti03022024backend.services.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/review")
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("post")
    public ResponseEntity<ReviewDtoIn> createReview(@Valid @RequestBody ReviewDtoIn reviewDtoIn) {
        ReviewDtoIn review = reviewService.createReview(reviewDtoIn);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @GetMapping("get/{bookId}")
    public ResponseEntity<List<ReviewDtoIn>> getReview(@PathVariable Long bookId) {
        List<ReviewDtoIn> reviews = reviewService.getBookReviews(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
