package ee.taltech.iti03022024backend.mappers.wishList;

import ee.taltech.iti03022024backend.dto.wishList.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishList.WishListDtoOut;
import ee.taltech.iti03022024backend.entities.wishList.WishListEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WishListMapper {

    WishListDtoOut toDto(WishListEntity wishListEntity);
    WishListEntity toEntity(WishListDtoIn wishListDtoIn);
    List<WishListDtoOut> toDtoList(List<WishListEntity> wishListEntityList);
}
