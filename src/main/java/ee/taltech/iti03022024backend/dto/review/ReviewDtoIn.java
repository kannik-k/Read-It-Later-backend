package ee.taltech.iti03022024backend.dto.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDtoIn {
    private long bookId;
    @NotNull
    @Size(min = 3, max = 500, message = "3-500 characters")
    private String review;
}
