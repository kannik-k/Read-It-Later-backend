package ee.taltech.iti03022024backend.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDtoInUsername {
    @NotNull
    @Size(min = 5, max = 15, message = "should be 5-15 characters long")
    private String username;
}
