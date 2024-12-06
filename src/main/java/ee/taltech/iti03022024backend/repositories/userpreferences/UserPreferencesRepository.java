package ee.taltech.iti03022024backend.repositories.userpreferences;

import ee.taltech.iti03022024backend.entities.usepreferences.UserPreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserPreferencesRepository extends JpaRepository<UserPreferencesEntity, Long>, JpaSpecificationExecutor<UserPreferencesEntity> {
    boolean existsByUserIdAndGenreId(Long userId, Long genreId);
    List<UserPreferencesEntity> findByUserIdAndGenreId(Long userId, Long genreId);
    void deleteByUserId(Long userId);
}
