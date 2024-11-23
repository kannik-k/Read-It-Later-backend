package ee.taltech.iti03022024backend.services.userpreferences;

import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.entities.usepreferences.UserPreferencesEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.userpreferences.UserPreferencesMapper;
import ee.taltech.iti03022024backend.repositories.userpreferences.UserPreferencesRepository;
import ee.taltech.iti03022024backend.services.genre.GenreService;
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
    private final GenreService genreService;

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
        return userPreferences.stream().map(entity -> {
            UserPreferencesDtoOut userPreferencesDtoOut = userPreferencesMapper.toDto(entity);
            String genre = genreService.getGenreById(entity.getGenreId());
            userPreferencesDtoOut.setGenreId(genre);
            return userPreferencesDtoOut;
        }).toList();
    }

    public void deleteGenre(Long userId, String genre) throws NotFoundException {
        Long genreId = genreService.getGenreByName(genre);
        if (genreId == null) {
            throw new NotFoundException("Genre not found: " + genre);
        }

        List<UserPreferencesEntity> userPreferences = userPreferencesRepository.findByUserIdAndGenreId(userId, genreId);

        if (userPreferences.isEmpty()) {
            throw new NotFoundException("No preferences found for user " + userId + " with genre " + genre);
        }

        userPreferencesRepository.deleteAll(userPreferences);
    }
}
