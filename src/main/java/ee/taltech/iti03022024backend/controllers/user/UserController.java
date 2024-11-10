package ee.taltech.iti03022024backend.controllers.user;

import ee.taltech.iti03022024backend.dto.user.UserDtoIn;
import ee.taltech.iti03022024backend.dto.user.UserDtoOut;
import ee.taltech.iti03022024backend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("post")
    public ResponseEntity<UserDtoOut> createUser(@Valid @RequestBody UserDtoIn userDtoIn) {
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
    public ResponseEntity<UserDtoOut> getUserById(@PathVariable long id) {
        UserDtoOut userDtoOut = userService.findUser(id);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @PutMapping("put/{id}") // May split into different parts in the future
    public ResponseEntity<UserDtoOut> updateUser(@PathVariable long id, @Valid @RequestBody UserDtoIn userDtoIn) {
        UserDtoOut userDtoOut = userService.updateUser(id, userDtoIn);
        return new ResponseEntity<>(userDtoOut, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
