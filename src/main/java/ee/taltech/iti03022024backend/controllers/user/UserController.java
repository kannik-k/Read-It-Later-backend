package ee.taltech.iti03022024backend.controllers.user;

import ee.taltech.iti03022024backend.dto.user.*;
import ee.taltech.iti03022024backend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("public/user")
    public ResponseEntity<LoginResponseDto> createUser(@Valid @RequestBody UserDtoIn userDtoIn) {
        LoginResponseDto loginResponseDto = userService.createUser(userDtoIn);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("public/user/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserDtoOut> getUserById(@PathVariable long id) {
        UserDtoOut userDtoOut = userService.findUser(id);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @PutMapping("user/{id}/username")
    public ResponseEntity<UserDtoOut> updateUserUsername(@PathVariable long id, @Valid @RequestBody UserDtoInUsername userDtoInUsername) {
        UserDtoOut userDtoOut = userService.updateUserUsername(id, userDtoInUsername);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @PutMapping("user/{id}/email")
    public ResponseEntity<UserDtoOut> updateUserEmail(@PathVariable long id, @Valid @RequestBody UserDtoInEmail userDtoInEmail) {
        UserDtoOut userDtoOut = userService.updateUserEmail(id, userDtoInEmail);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @PutMapping("user/{id}/password")
    public ResponseEntity<UserDtoOut> updateUserPassword(@PathVariable long id, @Valid @RequestBody UserPasswordChangeWrapper userPasswordChangeWrapper) {
        UserDtoOut userDtoOut = userService.updateUserPassword(id, userPasswordChangeWrapper);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
