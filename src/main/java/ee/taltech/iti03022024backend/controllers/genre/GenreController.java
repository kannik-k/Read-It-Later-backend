package ee.taltech.iti03022024backend.controllers.genre;

import ee.taltech.iti03022024backend.dto.genre.GenreDto;
import ee.taltech.iti03022024backend.services.genre.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api")
@RestController
@Tag(name = "Genre", description = "Api for managing genres.")
public class GenreController {
    private final GenreService genreService;

    @Operation(
            summary = "Return all genres",
            description = "Returns all genres from database. This table will never change. No new genres will be added."
    )
    @ApiResponse(responseCode = "200", description = "List is returned successfully.")
    @GetMapping("public/genre")
    public ResponseEntity<List<GenreDto>> getGenres() {
        return new ResponseEntity<>(genreService.getAllGenres(), HttpStatus.OK);
    }
}
