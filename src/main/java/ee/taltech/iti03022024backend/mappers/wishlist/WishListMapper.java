package ee.taltech.iti03022024backend.mappers.wishlist;

import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoIn;
import ee.taltech.iti03022024backend.dto.wishlist.WishListDtoOut;
import ee.taltech.iti03022024backend.entities.wishlist.WishListEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WishListMapper {

    WishListDtoOut toDto(WishListEntity wishListEntity);
    WishListEntity toEntity(WishListDtoIn wishListDtoIn);
    List<WishListDtoOut> toDtoList(List<WishListEntity> wishListEntityList);
}
