package ee.taltech.iti03022024backend.services.user;

import ee.taltech.iti03022024backend.dto.user.UserDto;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.exceptions.UnfilledFieldException;
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
        if (userDto.getUsername() == null || userDto.getEmail() == null || userDto.getPassword() == null) {
            throw new UnfilledFieldException("Please fill out all fields");
        }
        userRepository.save(userMapper.toEntity(userDto));
    }

    public UserDto findUser(long id) throws NotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));
        return userMapper.toDto(user);
    }

    public void updateUser(long id, UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getEmail() == null || userDto.getPassword() == null) {
            throw new UnfilledFieldException("Please fill out all fields");
        }
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Cannot update a user that does not exist"));
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setUsername(userDto.getUsername());
        userRepository.save(userEntity);
    }

    public void deleteUser(long id) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Cannot delete a user that does not exist"));
        userRepository.delete(userEntity);
    }
}
