package ee.taltech.iti03022024backend.dto.wishlist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for wish list. Clint sends to server.")
public class WishListDtoIn {
    @Schema(description = "User id, that shows which user's wish list it belongs to", example = "1")
    private long userId;
    @Schema(description = "Book id, that shows which shows that this book has been added to user's wish list", example = "3")
    private long bookId;
}
