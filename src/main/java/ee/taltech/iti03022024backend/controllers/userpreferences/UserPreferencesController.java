package ee.taltech.iti03022024backend.controllers.userpreferences;

import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.response.book.BookPageResponse;
import ee.taltech.iti03022024backend.services.userpreferences.UserPreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/user_preferences")
@RestController
@Tag(name = "User Preferences", description = "API for managing user preferences.")
public class UserPreferencesController {

    private final UserPreferencesService userPreferencesService;

    @Operation(
            summary = "Add genre to preferences.",
            description = "Adds a genre, user selects, to user preferences table."
    )
    @ApiResponse(responseCode = "200", description = "New genre has been added successfully.")
    @PostMapping()
    public ResponseEntity<UserPreferencesDtoOut> addGenre(Principal principal, @RequestBody UserPreferencesDtoIn userPreferencesDtoIn) {
        UserPreferencesDtoOut userPreferencesDtoOut = userPreferencesService.addGenre(userPreferencesDtoIn, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(userPreferencesDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Return user's preferences.",
            description = "Retrieves genres, user has added to preferences table."
    )
    @ApiResponse(responseCode = "200", description = "List of preferences have been retrieved successfully.")
    @GetMapping()
    public ResponseEntity<List<UserPreferencesDtoOut>> getUserPreferences(Principal principal) {
        List<UserPreferencesDtoOut> listOfPreferences = userPreferencesService.getGenres(Long.parseLong(principal.getName()));
        return new ResponseEntity<>(listOfPreferences, HttpStatus.OK);
    }

    @Operation(
            summary = "Return recommended books - books that have genres the user prefers.",
            description = "Returns books that the user should like using user's id and their preferred genres."
    )
    @ApiResponse(responseCode = "200", description = "Recommended books have been retrieved successfully.")
    @GetMapping("books")
    public ResponseEntity<BookPageResponse> getRecommendedBooks(Principal principal,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        BookPageResponse bookDtoOutList = userPreferencesService.getRecommendedBooks(Long.parseLong(principal.getName()), page, size);
        return new ResponseEntity<>(bookDtoOutList, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove preference.",
            description = "Removes genre, user has selected, from user preference table."
    )
    @ApiResponse(responseCode = "204", description = "Genre has been removed successfully.")
    @DeleteMapping("{genre}")
    public ResponseEntity<Void> deleteGenre(Principal principal, @PathVariable String genre) {
        userPreferencesService.deleteGenre(Long.parseLong(principal.getName()), genre);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
