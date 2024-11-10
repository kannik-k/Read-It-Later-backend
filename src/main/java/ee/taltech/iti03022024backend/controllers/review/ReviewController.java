package ee.taltech.iti03022024backend.controllers.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/review")
@RestController
public class ReviewController {
    //TODO service, entity, dtoOut?

    @PostMapping("post")
    public void createReview(@Valid @RequestBody ReviewDtoIn reviewDtoIn) {
        //TODO post method
    }
}
