package ee.taltech.iti03022024backend.dto.userPreferences;

import lombok.Data;

@Data
public class UserPreferencesDtoOut {
    private Long id;
    private Long userId;
    private Long genreId;
}
