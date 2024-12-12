package ee.taltech.iti03022024backend.controllers.book;

import ee.taltech.iti03022024backend.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class BookControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void createBook() throws Exception {
        String bookDtoIn = """
        {
            "title": "New Book Title",
            "author": "New Author",
            "genreId": 1
        }
        """;

        mvc.perform(post("/api/book")
                        .with(user("1").password("password1").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoIn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Book Title"))
                .andExpect(jsonPath("$.author").value("New Author"))
                .andExpect(jsonPath("$.genre").value("Fiction"));
    }

    @Test
    void getBookById() throws Exception {
        mvc.perform(get("/api/public/book/search_by_id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestTitle1"))
                .andExpect(jsonPath("$.author").value("TestAuthor1"))
                .andExpect(jsonPath("$.genre").value("Fiction"));
    }

    @Test
    void getBooks() throws Exception {
        mvc.perform(get("/api/public/book")
                        .param("author", "Author 1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "title-asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books.length()").value(1));
    }

    @Test
    void getBooksByGenre() throws Exception {
        mvc.perform(get("/api/public/book")
                        .param("genreId", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "title-asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books.length()").value(4))
                .andExpect(jsonPath("$.books[0].title").value("Book Title 1"))
                .andExpect(jsonPath("$.books[1].title").value("Book Title 3"));
    }
}
