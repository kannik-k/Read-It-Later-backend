package ee.taltech.iti03022024backend.controllers.user;

import ee.taltech.iti03022024backend.dto.user.UserDto;
import ee.taltech.iti03022024backend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
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

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        Optional<UserDto> user = userService.findUser(id);
        return user
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
