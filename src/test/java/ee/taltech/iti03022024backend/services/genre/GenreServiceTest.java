package ee.taltech.iti03022024backend.services.genre;

import ee.taltech.iti03022024backend.dto.genre.GenreDto;
import ee.taltech.iti03022024backend.entities.genre.GenreEntity;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.genre.GenreMapper;
import ee.taltech.iti03022024backend.mappers.genre.GenreMapperImpl;
import ee.taltech.iti03022024backend.repositories.genre.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;

    @Spy
    private GenreMapper genreMapper = new GenreMapperImpl();

    @InjectMocks
    private GenreService genreService;


    @Test
    void getAllGenres() {
        GenreDto genreOne = GenreDto.builder().genreId(1L).genre("Fiction").build();
        GenreEntity genreEntityOne = new GenreEntity();
        genreEntityOne.setGenreId(genreOne.getGenreId());
        genreEntityOne.setGenre(genreOne.getGenre());

        GenreDto genreTwo = GenreDto.builder().genreId(8L).genre("Horror").build();
        GenreEntity genreEntityTwo = new GenreEntity();
        genreEntityTwo.setGenreId(genreTwo.getGenreId());
        genreEntityTwo.setGenre(genreTwo.getGenre());

        List<GenreEntity> genreList = List.of(genreEntityOne, genreEntityTwo);
        when(genreRepository.findAll()).thenReturn(genreList);

        var result = genreService.getAllGenres();

        then(genreRepository).should().findAll();
        then(genreMapper).should().toDtoList(anyList());
        assertEquals(List.of(genreOne, genreTwo), result);
    }

    @Test
    void getGenreById() {
        long genreId = 2L;
        String genre = "Poetry";

        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setGenreId(genreId);
        genreEntity.setGenre(genre);

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genreEntity));

        String result = genreService.getGenreById(genreId);
        assertEquals(genre, result);
        verify(genreRepository).findById(genreId);
    }

    @Test
    void getGenreById_NotFound_throwsException() {
        Long genreId = 4L;
        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> genreService.getGenreById(genreId));

        assertEquals("Genre does not exist", exception.getMessage());
        verify(genreRepository).findById(genreId);
    }

    @Test
    void getGenreByName() {
        long genreId = 3L;
        String genre = "Thriller";

        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setGenreId(genreId);
        genreEntity.setGenre(genre);

        List<GenreEntity> genreEntities = List.of(genreEntity);

        when(genreRepository.findAll()).thenReturn(genreEntities);

        Long result = genreService.getGenreByName(genre);
        assertEquals(genreId, result);
        verify(genreRepository).findAll();
    }

    @Test
    void getGenreByName_NotFound_throwsException() {
        String genre = "NULL";

        List<GenreEntity> genreEntities = List.of();
        when(genreRepository.findAll()).thenReturn(genreEntities);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> genreService.getGenreByName(genre));
        assertEquals("Genre does not exist", exception.getMessage());

        verify(genreRepository).findAll();
    }
}