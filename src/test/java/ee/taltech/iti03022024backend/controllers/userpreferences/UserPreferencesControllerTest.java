package ee.taltech.iti03022024backend.controllers.userpreferences;

import ee.taltech.iti03022024backend.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserPreferencesControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getUserPreferences() throws Exception {
        mvc.perform(get("/api/user_preferences")
                        .with(user("3").password("password3").roles("USER"))) // Simulate authenticated user with ID "1"
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(3))
                .andExpect(jsonPath("$[0].genreId").value(1))
                .andExpect(jsonPath("$[0].genre").value("Fiction"));
    }


    @Test
    void addGenre() throws Exception {
        String genreRequest = """
    {
        "genreId": 7
    }
    """;

        mvc.perform(post("/api/user_preferences")
                        .with(user("1").password("password1").roles("USER"))
                        .contentType("application/json")
                        .content(genreRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.genreId").value(7))
                .andExpect(jsonPath("$.genre").value("Historical"))
                .andExpect(jsonPath("$.userId").value(1));
    }


    @Test
    void addGenre_whenGenreAlreadyExists_throwsException() throws Exception {
        String genreRequest = """
    {
        "genreId": 1
    }
    """;

        mvc.perform(post("/api/user_preferences")
                        .with(user("2").password("password2").roles("USER"))
                        .contentType("application/json")
                        .content(genreRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecommendedBooks_whenBooksFound_returnsBooks() throws Exception {
        mvc.perform(get("/api/user_preferences/books")
                        .param("page", "0")
                        .param("size", "5")
                        .with(user("2").password("password2").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(5))
                .andExpect(jsonPath("$.books[0].bookId").value(1))
                .andExpect(jsonPath("$.books[1].bookId").value(2))
                .andExpect(jsonPath("$.books[2].bookId").value(3))
                .andExpect(jsonPath("$.books[3].bookId").value(4))
                .andExpect(jsonPath("$.books.[4].bookId").value(6));
    }

    @Test
    void getRecommendedBooks_whenNoBooksFound_returnsEmptyResponse() throws Exception {
        mvc.perform(get("/api/user_preferences/books")
                        .param("page", "0")
                        .param("size", "5")
                        .with(user("6").password("password6").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(0));
    }

    @Test
    void getRecommendedBooks_whenNoPreferencesFound_returnsEmptyResponse() throws Exception {
        mvc.perform(get("/api/user_preferences/books")
                        .param("page", "0")
                        .param("size", "5")
                        .with(user("5").password("password5").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books.length()").value(0));
    }

    @Test
    void deleteGenre() throws Exception {
        mvc.perform(delete("/api/user_preferences/Fiction")
                        .with(user("4").password("password4").roles("USER")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteGenre_whenGenreNotFound_throwsException() throws Exception {
        mvc.perform(delete("/api/user_preferences/NotAGenre")
                        .with(user("4").password("password4").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGenre_whenUserHasNoGenres_throwsException() throws Exception {
        mvc.perform(delete("/api/user_preferences/Historical")
                        .with(user("5").password("password5").roles("USER")))
                .andExpect(status().isNotFound());
    }
}
