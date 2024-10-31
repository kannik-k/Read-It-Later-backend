package ee.taltech.iti03022024backend.dto.user;

import lombok.Data;

@Data
public class UserDtoIn {    // Client sends to server
    private String username;
    private String password;
    private String email;
}
