package ee.taltech.iti03022024backend.mappers.book;

import ee.taltech.iti03022024backend.dto.book.BookDtoIn;
import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    BookDtoOut toDto(BookEntity bookEntity);
    BookEntity toEntity(BookDtoIn bookDtoIn);
    List<BookDtoOut> toDtoList(List<BookEntity> bookEntityList);

}
