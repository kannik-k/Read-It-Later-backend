package ee.taltech.iti03022024backend.services.userpreferences;

import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.entities.userPreferences.UserPreferencesEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.userPreferences.UserPreferencesMapper;
import ee.taltech.iti03022024backend.repositories.userpreferences.UserPreferencesRepository;
import ee.taltech.iti03022024backend.specifications.userpreferences.UserPreferencesSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final UserPreferencesMapper userPreferencesMapper;

    public UserPreferencesDtoOut addGenre(UserPreferencesDtoIn userPreferencesDtoIn) {
        if (userPreferencesRepository.existsByUserIdAndGenreId(userPreferencesDtoIn.getUserId(), userPreferencesDtoIn.getGenreId())) {
            throw new NameAlreadyExistsException("Genre already exists.");
        }

        UserPreferencesEntity userPreferencesEntity = userPreferencesMapper.toEntity(userPreferencesDtoIn);
        userPreferencesRepository.save(userPreferencesEntity);
        return userPreferencesMapper.toDto(userPreferencesEntity);
    }

    public List<UserPreferencesDtoOut> getGenres(Long userId) {
        Specification<UserPreferencesEntity> spec = Specification.where(UserPreferencesSpecifications.getByUserId(userId));
        List<UserPreferencesEntity> userPreferences = userPreferencesRepository.findAll(spec);
        return userPreferencesMapper.toDtoList(userPreferences);
    }

    public void deleteGenre(Long userId, Long genreId) throws NotFoundException {
        if (!userPreferencesRepository.existsByUserIdAndGenreId(userId, genreId)) {
            throw new NotFoundException("Genre does not exist.");
        }
        Specification<UserPreferencesEntity> spec = Specification.where(UserPreferencesSpecifications.getByUserIdAndBookId(userId, genreId));
        List<UserPreferencesEntity> userPreferences = userPreferencesRepository.findAll(spec);
        userPreferencesRepository.deleteAll(userPreferences);
    }
}
