package ee.taltech.iti03022024backend.controllers.genre;

import ee.taltech.iti03022024backend.dto.genre.GenreDto;
import ee.taltech.iti03022024backend.services.genre.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api")
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping("public/genre")
    public List<GenreDto> getGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("public/genre/search_by_id/{id}")
    public ResponseEntity<String> getGenreById(@PathVariable long id) {
        String bookGenre = genreService.getGenreById(id);
        return new ResponseEntity<>(bookGenre, HttpStatus.OK);
    }
}
