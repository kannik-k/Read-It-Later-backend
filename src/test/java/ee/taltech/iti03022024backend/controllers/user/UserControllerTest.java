package ee.taltech.iti03022024backend.controllers.user;

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
@AutoConfigureMockMvc
@Transactional
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void createUser() throws Exception {
        String userDtoIn = """
        {
            "username": "newUser",
            "password": "newPassword",
            "passwordAgain": "newPassword",
            "email": "newuser@example.com"
        }
        """;

        mvc.perform(post("/api/public/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoIn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login() throws Exception {
        String loginRequest = """
        {
            "username": "existingUser",
            "password": "password7"
        }
        """;

        mvc.perform(post("/api/public/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void getUserById() throws Exception {
        mvc.perform(get("/api/user")
                        .with(user("7").password("password7").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(7L))
                .andExpect(jsonPath("$.username").value("existingUser"))
                .andExpect(jsonPath("$.email").value("email@example.com"));
    }

    @Test
    void updateUserUsername() throws Exception {
        String updateUsername = """
        {
            "username": "newUsername"
        }
        """;

        mvc.perform(put("/api/user/username")
                        .with(user("7").password("password7").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUsername"));
    }

    @Test
    void updateUserEmail() throws Exception {
        String updateEmail = """
        {
            "email": "new.email@example.com"
        }
        """;

        mvc.perform(put("/api/user/email")
                        .with(user("7").password("password7").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new.email@example.com"));
    }

    @Test
    void updateUserPassword() throws Exception {
        String updatePassword = """
        {
            "oldPassword": "oldPassword",
            "newPassword": "newPassword",
            "confirmNewPassword": "newPassword"
        }
        """;

        mvc.perform(put("/api/user/password")
                        .with(user("7").password("oldPassword").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePassword))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/api/user")
                        .with(user("7").password("password").roles("USER")))
                .andExpect(status().isNoContent());
    }
}
