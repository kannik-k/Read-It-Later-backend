package ee.taltech.iti03022024backend.controllers.genre;

import ee.taltech.iti03022024backend.dto.genre.GenreDto;
import ee.taltech.iti03022024backend.services.genre.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/genre")
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping("get")
    public List<GenreDto> getGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable long id) {
        GenreDto genreDto = genreService.getGenreById(id);
        return new ResponseEntity<>(genreDto, HttpStatus.OK);
    }

    @GetMapping("getByName/{genre}")
    public ResponseEntity<GenreDto> getGenreByName(@PathVariable String genre) {
        GenreDto genreDto = genreService.getGenreByName(genre);
        return new ResponseEntity<>(genreDto, HttpStatus.OK);
    }
}
