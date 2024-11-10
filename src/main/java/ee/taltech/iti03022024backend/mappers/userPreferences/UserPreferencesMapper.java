package ee.taltech.iti03022024backend.mappers.userPreferences;

import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.entities.userPreferences.UserPreferencesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPreferencesMapper {
    UserPreferencesDtoOut toDto(UserPreferencesEntity userPreferencesEntity);
    UserPreferencesEntity toEntity(UserPreferencesDtoIn userPreferencesDtoIn);
    List<UserPreferencesDtoOut> toDtoList(List<UserPreferencesEntity> userPreferencesEntityList);
}
