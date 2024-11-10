package ee.taltech.iti03022024backend.controllers.userPreferences;

import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userPreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.services.userPreferences.UserPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/user_preferences")
@Controller
public class UserPreferencesController {

    private final UserPreferencesService userPreferencesService;

    @PostMapping("post")
    public ResponseEntity<UserPreferencesDtoOut> addGenre(@RequestBody UserPreferencesDtoIn userPreferencesDtoIn) {
        UserPreferencesDtoOut userPreferencesDtoOut = userPreferencesService.addGenre(userPreferencesDtoIn);
        return new ResponseEntity<>(userPreferencesDtoOut, HttpStatus.OK);
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<List<UserPreferencesDtoOut>> getUserPreferences(@PathVariable Long userId) {
        List<UserPreferencesDtoOut> listOfPreferences = userPreferencesService.getGenres(userId);
        return new ResponseEntity<>(listOfPreferences, HttpStatus.OK);
    }

    @DeleteMapping("delete/{userId}/{genreId}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long userId, @PathVariable Long genreId) {
        userPreferencesService.deleteGenre(userId, genreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
