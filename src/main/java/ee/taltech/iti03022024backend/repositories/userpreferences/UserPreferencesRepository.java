package ee.taltech.iti03022024backend.repositories.userpreferences;

import ee.taltech.iti03022024backend.entities.userPreferences.UserPreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserPreferencesRepository extends JpaRepository<UserPreferencesEntity, Long>, JpaSpecificationExecutor<UserPreferencesEntity> {
    boolean existsByUserIdAndGenreId(Long userId, Long genreId);
}
