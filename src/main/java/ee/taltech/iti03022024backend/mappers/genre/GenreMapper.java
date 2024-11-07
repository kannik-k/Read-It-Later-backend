package ee.taltech.iti03022024backend.mappers.genre;

import ee.taltech.iti03022024backend.dto.genre.GenreDto;
import ee.taltech.iti03022024backend.entities.genre.GenreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMapper {

    GenreDto toDto(GenreEntity genreEntity);
    List<GenreDto> toDtoList(List<GenreEntity> genreEntities);
}
