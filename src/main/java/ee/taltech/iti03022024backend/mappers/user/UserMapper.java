package ee.taltech.iti03022024backend.mappers.user;

import ee.taltech.iti03022024backend.dto.user.UserDtoIn;
import ee.taltech.iti03022024backend.dto.user.UserDtoOut;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDtoOut toDto(UserEntity userEntity);
    UserEntity toEntity(UserDtoIn userDtoIn);
    List<UserDtoOut> toDtoList(List<UserEntity> userEntityList);
}
