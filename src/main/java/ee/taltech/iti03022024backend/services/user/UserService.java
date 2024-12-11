package ee.taltech.iti03022024backend.services.user;

import ee.taltech.iti03022024backend.dto.user.*;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import ee.taltech.iti03022024backend.exceptions.IncorrectInputException;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.user.UserMapper;
import ee.taltech.iti03022024backend.repositories.user.UserRepository;
import ee.taltech.iti03022024backend.security.JwtGenerator;
import ee.taltech.iti03022024backend.services.userpreferences.UserPreferencesService;
import ee.taltech.iti03022024backend.services.wishlist.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final WishListService wishListService;
    private  final UserPreferencesService userPreferencesService;
    private static final String USER_NONEXISTENT = "Cannot update a user that does not exist";

    public LoginResponseDto createUser(UserDtoIn userDtoIn) throws NameAlreadyExistsException, IncorrectInputException {
        if (userRepository.existsByUsername(userDtoIn.getUsername())) {
            throw new NameAlreadyExistsException("Username already exists");
        }
        if (!Objects.equals(userDtoIn.getPassword(), userDtoIn.getPasswordAgain())) {
            throw new IncorrectInputException("Password fields do not match");
        }
        UserEntity userEntity = userMapper.toEntity(userDtoIn);
        userEntity.setPassword(passwordEncoder.encode(userDtoIn.getPassword()));
        userRepository.save(userEntity);
        String token = jwtGenerator.generateToken(userEntity);
        return new LoginResponseDto(token);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws NotFoundException, IncorrectInputException {
        if (!userRepository.existsByUsername(loginRequestDto.getUsername())) {
            throw new NotFoundException("Username doesn't exist");
        }
        UserEntity userEntity = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(() -> new NotFoundException("User does not exist"));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getPassword())) {
            throw new IncorrectInputException("Invalid password");
        }
        String token = jwtGenerator.generateToken(userEntity);
        return new LoginResponseDto(token);
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

    public UserDtoOut updateUserPassword(long id, UserPasswordChangeWrapper userPasswordChangeWrapper) throws NotFoundException, IncorrectInputException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NONEXISTENT));
        if (!passwordEncoder.matches(userPasswordChangeWrapper.getOldPassword(), userEntity.getPassword())) {
            throw new IncorrectInputException("Old password is incorrect");
        } else if (!Objects.equals(userPasswordChangeWrapper.getNewPassword(), userPasswordChangeWrapper.getConfirmNewPassword())) {
            throw new IncorrectInputException("New password fields do not match");
        }
        userEntity.setPassword(passwordEncoder.encode(userPasswordChangeWrapper.getNewPassword()));
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Transactional
    public void deleteUser(long id) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Cannot delete a user that does not exist"));
        wishListService.deleteByUserId(id);
        userPreferencesService.deleteByUserId(id);
        userRepository.delete(userEntity);
    }
}
