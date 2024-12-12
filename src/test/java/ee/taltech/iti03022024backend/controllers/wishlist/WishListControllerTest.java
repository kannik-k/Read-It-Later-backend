package ee.taltech.iti03022024backend.controllers.wishlist;

import ee.taltech.iti03022024backend.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishListControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Test
    void addToWishList_whenCorrect_returnsWishListDto() throws Exception {
        String wishListRequest = """
    {
        "bookId": 1
    }
    """;
        mvc.perform(post("/api/wish_list")
                        .with(user("3").password("password3").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON).content(wishListRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.userId").value(3));
    }

    @Test
    void addToWishList_whenAlreadyExists_throwsException() throws Exception {
        String wishListRequest = """
    {
        "bookId": 1
    }
    """;
        mvc.perform(post("/api/wish_list")
                        .with(user("1").password("password1").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON).content(wishListRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWishList_whenCorrect_returnsBooks() throws Exception {
        mvc.perform(get("/api/wish_list")
                        .with(user("1").password("password1").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(2))
                .andExpect(jsonPath("$.books[0].bookId").value(1))
                .andExpect(jsonPath("$.books[1].bookId").value(2));

    }

    @Test
    void getWishList_whenNoBooksAddedYet_returnsEmptyResponse() throws Exception {
        mvc.perform(get("/api/wish_list")
                        .with(user("3").password("password3").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(0));

    }

    @Test
    void deleteBookFromWishList_whenCorrect_returnsNoContent() throws Exception {
        mvc.perform(delete("/api/wish_list/1")
                        .with(user("1").password("password1").roles("USER")))
                .andExpect(status().isNoContent());
    }
}
