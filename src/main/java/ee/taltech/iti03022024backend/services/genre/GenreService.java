package ee.taltech.iti03022024backend.services.genre;

import ee.taltech.iti03022024backend.dto.genre.GenreDto;
import ee.taltech.iti03022024backend.entities.genre.GenreEntity;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.genre.GenreMapper;
import ee.taltech.iti03022024backend.repositories.genre.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreMapper genreMapper;
    private final GenreRepository genreRepository;

    public List<GenreDto> getAllGenres() {
        return genreMapper.toDtoList(genreRepository.findAll());
    }

    public String getGenreById(long id) throws NotFoundException {
        GenreEntity genre = genreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre does not exist"));
        return genre.getGenre();
    }

    public GenreDto getGenreByName(String name) throws NotFoundException {
        List<GenreEntity> genres = genreRepository.findAll();
        GenreEntity genre = genres.stream()
                .filter(genreName-> genreName.getGenre().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Genre does not exist"));
        return genreMapper.toDto(genre);
    }
}
