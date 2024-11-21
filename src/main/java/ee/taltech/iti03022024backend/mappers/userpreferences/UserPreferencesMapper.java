package ee.taltech.iti03022024backend.mappers.userpreferences;

import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.entities.usepreferences.UserPreferencesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPreferencesMapper {
    UserPreferencesDtoOut toDto(UserPreferencesEntity userPreferencesEntity);
    UserPreferencesEntity toEntity(UserPreferencesDtoIn userPreferencesDtoIn);
    List<UserPreferencesDtoOut> toDtoList(List<UserPreferencesEntity> userPreferencesEntityList);
}
