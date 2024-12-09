package ee.taltech.iti03022024backend.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "Data transfer object for review. Client sends to server.")
public class ReviewDtoIn {
    @Schema(description = "Book id that shows which book is the review about.", example = "1")
    private long bookId;
    @NotNull
    @Size(min = 3, max = 500, message = "3-500 characters")
    @Schema(description = "Book review.", example = "Best book ever written!")
    private String review;
}
