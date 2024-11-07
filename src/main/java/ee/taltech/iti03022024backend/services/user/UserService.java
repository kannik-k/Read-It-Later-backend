package ee.taltech.iti03022024backend.services.user;

import ee.taltech.iti03022024backend.dto.user.UserDtoIn;
import ee.taltech.iti03022024backend.dto.user.UserDtoOut;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.exceptions.UnfilledFieldException;
import ee.taltech.iti03022024backend.mappers.user.UserMapper;
import ee.taltech.iti03022024backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDtoOut createUser(UserDtoIn userDtoIn) {
        if (userDtoIn.getUsername() == null || userDtoIn.getEmail() == null || userDtoIn.getPassword() == null) {
            throw new UnfilledFieldException("Please fill out all fields");
        }
        if (userRepository.existsByUsername(userDtoIn.getUsername())) {
            throw new NameAlreadyExistsException("Username already exists");
        }
        UserEntity userEntity = userMapper.toEntity(userDtoIn);
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public List<UserDtoOut> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDtoOut findUser(long id) throws NotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));
        return userMapper.toDto(user);
    }

    public UserDtoOut updateUser(long id, UserDtoIn userDtoIn) {
        if (userDtoIn.getUsername() == null || userDtoIn.getEmail() == null || userDtoIn.getPassword() == null) {
            throw new UnfilledFieldException("Please fill out all fields");
        }
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Cannot update a user that does not exist"));
        userEntity.setEmail(userDtoIn.getEmail());
        userEntity.setPassword(userDtoIn.getPassword());
        userEntity.setUsername(userDtoIn.getUsername());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public void deleteUser(long id) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Cannot delete a user that does not exist"));
        userRepository.delete(userEntity);
    }
}
