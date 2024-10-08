package ee.taltech.iti03022024backend.dto.user;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String email;
}
