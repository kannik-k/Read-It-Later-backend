package ee.taltech.iti03022024backend.controllers.user;

import ee.taltech.iti03022024backend.dto.user.UserDto;
import ee.taltech.iti03022024backend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping()
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping()
    public void createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @GetMapping()
    public String hello() {
        return "HELLO WORLD!!!";
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        Optional<UserDto> user = userService.findUser(id);
        return user
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("{id}") // May split into different parts in the future
    public ResponseEntity<Void> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        userService.updateUser(id, userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
