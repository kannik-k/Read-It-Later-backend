package ee.taltech.iti03022024backend.dto.user;

import lombok.Data;

@Data
public class UserDtoOut {   // Server sends to client
    private long userId;
    private String username;
    private String email;
}
