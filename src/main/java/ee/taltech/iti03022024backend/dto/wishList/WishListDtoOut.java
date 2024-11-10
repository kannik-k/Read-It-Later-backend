package ee.taltech.iti03022024backend.dto.wishList;

import lombok.Data;

@Data
public class WishListDtoOut {
    private long id;
    private long userId;
    private long bookId;
}
