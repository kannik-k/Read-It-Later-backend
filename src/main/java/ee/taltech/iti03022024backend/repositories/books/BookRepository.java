package ee.taltech.iti03022024backend.repositories.books;

import ee.taltech.iti03022024backend.entities.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
