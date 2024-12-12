package ee.taltech.iti03022024backend.controllers.review;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void createReview() throws Exception {
        String reviewDtoIn = """
        {
            "bookId": 1,
            "review": "Great book!"
        }
        """;

        mvc.perform(post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewDtoIn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.review").value("Great book!"))
                .andExpect(jsonPath("$.bookId").value(1));
    }

    @Test
    void getReview() throws Exception {

        mvc.perform(get("/api/public/review/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].review").value("Good"))
                .andExpect(jsonPath("$.content[1].review").value("bad"));
    }
}
