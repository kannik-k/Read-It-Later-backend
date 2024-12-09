package ee.taltech.iti03022024backend.repositories.books;

import ee.taltech.iti03022024backend.entities.book.BookEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity>, PagingAndSortingRepository<BookEntity, Long> {

    Slice<BookEntity> findAllByGenreIdIn(List<Long> genreIds, Pageable pageable);
}
