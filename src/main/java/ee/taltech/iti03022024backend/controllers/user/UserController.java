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

import java.security.Principal;

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

    @Operation(
            summary = "Log in user.",
            description = "Checks if username and password are correct and sends back new token if login is successful."
    )
    @ApiResponse(responseCode = "200", description = "User has been logged in successfully.")
    @PostMapping("public/user/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve user from database.",
            description = "Retrieve user by userId form database."
    )
    @ApiResponse(responseCode = "200", description = "User has been retrieved successfully from database.")
    @GetMapping("user")
    public ResponseEntity<UserDtoOut> getUserById(Principal principal) {
        UserDtoOut userDtoOut = userService.findUser(Long.parseLong(principal.getName()));
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Change user's username.",
            description = "User can change username that they currently have."
    )
    @ApiResponse(responseCode = "200", description = "Username has been changed successfully.")
    @PutMapping("user/username")
    public ResponseEntity<UserDtoOut> updateUserUsername(Principal principal, @Valid @RequestBody UserDtoInUsername userDtoInUsername) {
        UserDtoOut userDtoOut = userService.updateUserUsername(Long.parseLong(principal.getName()), userDtoInUsername);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Change user's email.",
            description = "User can change email that they currently have."
    )
    @ApiResponse(responseCode = "200", description = "Email has been changed successfully.")
    @PutMapping("user/email")
    public ResponseEntity<UserDtoOut> updateUserEmail(Principal principal, @Valid @RequestBody UserDtoInEmail userDtoInEmail) {
        UserDtoOut userDtoOut = userService.updateUserEmail(Long.parseLong(principal.getName()), userDtoInEmail);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Change user's password.",
            description = "User can change password that they currently have."
    )
    @ApiResponse(responseCode = "200", description = "Password has been changed successfully.")
    @PutMapping("user/password")
    public ResponseEntity<UserDtoOut> updateUserPassword(Principal principal, @Valid @RequestBody UserPasswordChangeWrapper userPasswordChangeWrapper) {
        UserDtoOut userDtoOut = userService.updateUserPassword(Long.parseLong(principal.getName()), userPasswordChangeWrapper);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user.",
            description = "User can delete their account from database."
    )
    @ApiResponse(responseCode = "204", description = "User has been deleted successfully.")
    @DeleteMapping("user")
    public ResponseEntity<Void> deleteUser(Principal principal) {
        userService.deleteUser(Long.parseLong(principal.getName()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
