package ee.taltech.iti03022024backend.mappers.review;

import ee.taltech.iti03022024backend.dto.review.ReviewDtoIn;
import ee.taltech.iti03022024backend.entities.review.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewDtoIn toDto(ReviewEntity reviewEntity);
    ReviewEntity toEntity(ReviewDtoIn reviewDtoIn);
    List<ReviewDtoIn> toDtoList(List<ReviewEntity> reviewEntityList);
}
