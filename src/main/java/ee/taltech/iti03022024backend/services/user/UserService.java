package ee.taltech.iti03022024backend.services.user;

import ee.taltech.iti03022024backend.dto.user.UserDto;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import ee.taltech.iti03022024backend.mappers.user.UserMapper;
import ee.taltech.iti03022024backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void createUser(UserDto userDto) {
        userRepository.save(userMapper.toEntity(userDto));
    }

    public Optional<UserDto> findUser(long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        return userOptional.map(userMapper::toDto); // Returns Optional.empty if optional is not present, otherwise maps it to dto
    }
}
