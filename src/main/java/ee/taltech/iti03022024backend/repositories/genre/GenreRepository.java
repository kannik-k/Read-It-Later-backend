package ee.taltech.iti03022024backend.repositories.genre;

import ee.taltech.iti03022024backend.entities.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
}
