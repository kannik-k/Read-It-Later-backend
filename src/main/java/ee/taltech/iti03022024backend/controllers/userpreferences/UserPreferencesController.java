package ee.taltech.iti03022024backend.controllers.userpreferences;

import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.services.userpreferences.UserPreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserPreferencesDtoOut> addGenre(@RequestBody UserPreferencesDtoIn userPreferencesDtoIn) {
        UserPreferencesDtoOut userPreferencesDtoOut = userPreferencesService.addGenre(userPreferencesDtoIn);
        return new ResponseEntity<>(userPreferencesDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Return user's preferences.",
            description = "Retrieves genres, user has added to preferences table."
    )
    @ApiResponse(responseCode = "200", description = "List of preferences have been retrieved successfully.")
    @GetMapping("{userId}")
    public ResponseEntity<List<UserPreferencesDtoOut>> getUserPreferences(@PathVariable Long userId) {
        List<UserPreferencesDtoOut> listOfPreferences = userPreferencesService.getGenres(userId);
        return new ResponseEntity<>(listOfPreferences, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove preference.",
            description = "Removes genre, user has selected, from user preference table."
    )
    @ApiResponse(responseCode = "204", description = "Genre has been removed successfully.")
    @DeleteMapping("{userId}/{genre}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long userId, @PathVariable String genre) {
        userPreferencesService.deleteGenre(userId, genre);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
