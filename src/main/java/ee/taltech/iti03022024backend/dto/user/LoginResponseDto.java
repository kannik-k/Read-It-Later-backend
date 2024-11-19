package ee.taltech.iti03022024backend.dto.user;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String token;

    public LoginResponseDto(String token){
        this.token = token;
    }
}
