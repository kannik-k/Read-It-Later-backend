package ee.taltech.iti03022024backend.response.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ReviewPageResponse {
    private List<ReviewDtoIn> reviews;
    private boolean hasNextPage;

    public ReviewPageResponse(List<ReviewDtoIn> reviews, boolean hasNextPage) {
        this.reviews = reviews;
        this.hasNextPage = hasNextPage;
    }
}
