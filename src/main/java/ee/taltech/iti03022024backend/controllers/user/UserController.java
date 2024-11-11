package ee.taltech.iti03022024backend.controllers.user;

import ee.taltech.iti03022024backend.dto.user.UserDtoIn;
import ee.taltech.iti03022024backend.dto.user.UserDtoOut;
import ee.taltech.iti03022024backend.dto.user.UserPasswordChangeWrapper;
import ee.taltech.iti03022024backend.exceptions.IncorrectInputException;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("post")
    public ResponseEntity<UserDtoOut> createUser(@Valid @RequestBody UserDtoIn userDtoIn) throws NameAlreadyExistsException {
        UserDtoOut userDtoOut = userService.createUser(userDtoIn);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @GetMapping()
    public String hello() {
        return "Welcome to Read It Later!!!";
    }

    @GetMapping("get")
    public List<UserDtoOut> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<UserDtoOut> getUserById(@PathVariable long id) throws NotFoundException {
        UserDtoOut userDtoOut = userService.findUser(id);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @PutMapping("/put/{id}/username")
    public ResponseEntity<UserDtoOut> updateUserUsername(@PathVariable long id, @Valid @RequestBody UserDtoIn userDtoIn) throws NotFoundException, NameAlreadyExistsException {
        UserDtoOut userDtoOut = userService.updateUserUsername(id, userDtoIn);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @PutMapping("put/{id}/email")
    public ResponseEntity<UserDtoOut> updateUserEmail(@PathVariable long id, @Valid @RequestBody UserDtoIn userDtoIn) throws NotFoundException {
        UserDtoOut userDtoOut = userService.updateUserEmail(id, userDtoIn);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @PutMapping("put/{id}/password")
    public ResponseEntity<UserDtoOut> updateUserPassword(@PathVariable long id, @Valid @RequestBody UserPasswordChangeWrapper userPasswordChangeWrapper) throws NotFoundException, IncorrectInputException {
        UserDtoOut userDtoOut = userService.updateUserPassword(id, userPasswordChangeWrapper);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws NotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
