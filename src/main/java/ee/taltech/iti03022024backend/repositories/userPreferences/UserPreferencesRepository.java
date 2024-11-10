package ee.taltech.iti03022024backend.repositories.userPreferences;

import ee.taltech.iti03022024backend.entities.userPreferences.UserPreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferencesEntity, Long> {
    boolean existsByUserIdAndGenreId(Long userId, Long genreId);
}
