package ee.taltech.iti03022024backend.services.userPreferences;

import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.entities.userPreferences.UserPreferencesEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.userPreferences.UserPreferencesMapper;
import ee.taltech.iti03022024backend.repositories.userPreferences.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<UserPreferencesEntity> listOfGenres = userPreferencesRepository.findAll().stream()
                .filter(preference -> preference.getUserId().equals(userId))
                .collect(Collectors.toList());

        return userPreferencesMapper.toDtoList(listOfGenres);
    }

    public void deleteGenre(Long userId, Long genreId) throws NotFoundException {
        if (!userPreferencesRepository.existsByUserIdAndGenreId(userId, genreId)) {
            throw new NotFoundException("Genre does not exist.");
        }

        List<UserPreferencesEntity> userPreferencesEntityList = userPreferencesRepository.findAll().stream()
                .filter(preferences -> preferences.getUserId().equals(userId) && preferences.getGenreId().equals(genreId))
                .collect(Collectors.toList());

        userPreferencesRepository.deleteAll(userPreferencesEntityList);
    }
}
