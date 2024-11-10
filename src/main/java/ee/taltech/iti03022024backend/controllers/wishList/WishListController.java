package ee.taltech.iti03022024backend.controllers.wishList;

import ee.taltech.iti03022024backend.dto.wishList.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishList.WishListDtoOut;
import ee.taltech.iti03022024backend.services.wishList.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/wish_list")
@Controller
public class WishListController {

    private final WishListService wishListService;

    // Post might need changing depending on the frontend
    @PostMapping("post")
    public ResponseEntity<WishListDtoOut> addToWishList(@RequestBody WishListDtoIn wishListDtoIn) {
        WishListDtoOut wishListDtoOut = wishListService.addToWishList(wishListDtoIn);
        return new ResponseEntity<>(wishListDtoOut, HttpStatus.OK);
    }

    //get by user id
    @GetMapping("get/{userId}")
    public ResponseEntity<List<WishListDtoOut>> getWishList(@PathVariable Long userId) {
        List<WishListDtoOut> wishListDtoOutList = wishListService.getUserBooks(userId);
        return new ResponseEntity<>(wishListDtoOutList, HttpStatus.OK);
    }

    //delete takes
    @DeleteMapping("delete/{userId}/{bookId}")
    public ResponseEntity<Void> deleteFromWishList(@PathVariable Long userId, @PathVariable Long bookId) {
        wishListService.deleteBookFromWishList(userId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
