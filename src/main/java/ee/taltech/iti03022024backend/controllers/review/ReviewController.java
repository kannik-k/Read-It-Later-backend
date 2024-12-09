package ee.taltech.iti03022024backend.controllers.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import ee.taltech.iti03022024backend.response.review.ReviewPageResponse;
import ee.taltech.iti03022024backend.services.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api")
@RestController
@Tag(name = "Reviews", description = "API for managing reviews.")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(
            summary = "Create new review for a book.",
            description = "Add new review to a book."
    )
    @ApiResponse(responseCode = "200", description = "Review is successfully added.")
    @PostMapping("review")
    public ResponseEntity<ReviewDtoIn> createReview(@Valid @RequestBody ReviewDtoIn reviewDtoIn) {
        ReviewDtoIn review = reviewService.createReview(reviewDtoIn);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all book reviews.",
            description = "Get all book reviews from database, based on bookId."
    )
    @ApiResponse(responseCode = "200", description = "Book reviews have been retrieved successfully.")
    @GetMapping("public/review/{bookId}")
    public ResponseEntity<ReviewPageResponse> getReview(@PathVariable Long bookId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        ReviewPageResponse reviews = reviewService.getBookReviews(bookId, page, size);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
