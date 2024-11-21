package ee.taltech.iti03022024backend.dto.wishlist;

import lombok.Data;

@Data
public class WishListDtoIn {
    private long userId;
    private long bookId;
}
