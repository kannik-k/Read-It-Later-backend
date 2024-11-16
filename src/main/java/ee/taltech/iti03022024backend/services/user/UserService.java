package ee.taltech.iti03022024backend.services.user;

import ee.taltech.iti03022024backend.dto.user.*;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import ee.taltech.iti03022024backend.exceptions.IncorrectInputException;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.user.UserMapper;
import ee.taltech.iti03022024backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String USER_NONEXISTENT = "Cannot update a user that does not exist";

    public UserDtoOut createUser(UserDtoIn userDtoIn) throws NameAlreadyExistsException, IncorrectInputException {
        if (userRepository.existsByUsername(userDtoIn.getUsername())) {
            throw new NameAlreadyExistsException("Username already exists");
        }
        if (!Objects.equals(userDtoIn.getPassword(), userDtoIn.getPasswordAgain())) {
            throw new IncorrectInputException("Password fields do not match");
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

    public UserDtoOut updateUserUsername(long id, UserDtoInUsername userDtoInUsername) throws NotFoundException, NameAlreadyExistsException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NONEXISTENT));
        if (userRepository.existsByUsername(userDtoInUsername.getUsername())) {
            throw new NameAlreadyExistsException("Username already exists");
        }
        userEntity.setUsername(userDtoInUsername.getUsername());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public UserDtoOut updateUserEmail(long id, UserDtoInEmail userDtoInEmail) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NONEXISTENT));
        userEntity.setEmail(userDtoInEmail.getEmail());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    // Later: passwordEncoder should be used here
    public UserDtoOut updateUserPassword(long id, UserPasswordChangeWrapper userPasswordChangeWrapper) throws NotFoundException, IncorrectInputException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NONEXISTENT));
        if (!Objects.equals(userPasswordChangeWrapper.getOldPassword(), userEntity.getPassword())) {
            throw new IncorrectInputException("Old password is incorrect");
        } else if (!Objects.equals(userPasswordChangeWrapper.getNewPassword(), userPasswordChangeWrapper.getConfirmNewPassword())) {
            throw new IncorrectInputException("New password fields do not match");
        }
        userEntity.setPassword(userPasswordChangeWrapper.getNewPassword());
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public void deleteUser(long id) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Cannot delete a user that does not exist"));
        userRepository.delete(userEntity);
    }
}
