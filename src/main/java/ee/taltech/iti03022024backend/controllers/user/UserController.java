package ee.taltech.iti03022024backend.controllers.user;

import ee.taltech.iti03022024backend.dto.user.*;
import ee.taltech.iti03022024backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api")
@RestController
@Tag(name = "User", description = "API for managing user.")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Create new user.",
            description = "Creates new user based on given values."
    )
    @ApiResponse(responseCode = "200", description = "User has been created successfully.")
    @PostMapping("public/user")
    public ResponseEntity<LoginResponseDto> createUser(@Valid @RequestBody UserDtoIn userDtoIn) {
        LoginResponseDto loginResponseDto = userService.createUser(userDtoIn);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("public/user/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @Operation(
            summary = "Retrieve user from database.",
            description = "Retrieve user by userId form database."
    )
    @ApiResponse(responseCode = "200", description = "User has been retrieved successfully from database.")
    @GetMapping("user/{id}")
    public ResponseEntity<UserDtoOut> getUserById(@PathVariable long id) {
        UserDtoOut userDtoOut = userService.findUser(id);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Change user's username.",
            description = "User can change username that they currently have."
    )
    @ApiResponse(responseCode = "200", description = "Username has been changed successfully.")
    @PutMapping("user/{id}/username")
    public ResponseEntity<UserDtoOut> updateUserUsername(@PathVariable long id, @Valid @RequestBody UserDtoInUsername userDtoInUsername) {
        UserDtoOut userDtoOut = userService.updateUserUsername(id, userDtoInUsername);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Change user's email.",
            description = "User can change email that they currently have."
    )
    @ApiResponse(responseCode = "200", description = "Email has been changed successfully.")
    @PutMapping("user/{id}/email")
    public ResponseEntity<UserDtoOut> updateUserEmail(@PathVariable long id, @Valid @RequestBody UserDtoInEmail userDtoInEmail) {
        UserDtoOut userDtoOut = userService.updateUserEmail(id, userDtoInEmail);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Change user's password.",
            description = "User can change password that they currently have."
    )
    @ApiResponse(responseCode = "200", description = "Password has been changed successfully.")
    @PutMapping("user/{id}/password")
    public ResponseEntity<UserDtoOut> updateUserPassword(@PathVariable long id, @Valid @RequestBody UserPasswordChangeWrapper userPasswordChangeWrapper) {
        UserDtoOut userDtoOut = userService.updateUserPassword(id, userPasswordChangeWrapper);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user.",
            description = "User can delete their account from database."
    )
    @ApiResponse(responseCode = "204", description = "User has been deleted successfully.")
    @DeleteMapping("user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
