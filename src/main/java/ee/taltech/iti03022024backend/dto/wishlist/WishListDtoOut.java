package ee.taltech.iti03022024backend.dto.wishlist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data transfer object for wish list. Server sends to client.")
public class WishListDtoOut {
    @Schema(description = "Unique id.", example = "1")
    private long id;
    @Schema(description = "User id, that shows which user's wish list it belongs to", example = "3")
    private long userId;
    @Schema(description = "Book id, that shows which shows that this book has been added to user's wish list", example = "5")
    private long bookId;
}
