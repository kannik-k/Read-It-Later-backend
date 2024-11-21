package ee.taltech.iti03022024backend.dto.userpreferences;

import lombok.Data;

@Data
public class UserPreferencesDtoOut {
    private Long id;
    private Long userId;
    private Long genreId;
}
