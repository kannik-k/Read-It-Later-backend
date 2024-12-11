package ee.taltech.iti03022024backend.services.user;

import ee.taltech.iti03022024backend.dto.user.*;
import ee.taltech.iti03022024backend.entities.user.UserEntity;
import ee.taltech.iti03022024backend.exceptions.IncorrectInputException;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.user.UserMapper;
import ee.taltech.iti03022024backend.mappers.user.UserMapperImpl;
import ee.taltech.iti03022024backend.repositories.user.UserRepository;
import ee.taltech.iti03022024backend.security.JwtGenerator;
import ee.taltech.iti03022024backend.services.userpreferences.UserPreferencesService;
import ee.taltech.iti03022024backend.services.wishlist.WishListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtGenerator jwtGenerator;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private WishListService wishListService;
    @Mock
    private UserPreferencesService userPreferencesService;
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @InjectMocks
    private UserService userService;

    @Test
    void createUser_whenSuccessful_returnsCorrectResponse() {
        UserDtoIn userDtoIn = UserDtoIn.builder().password("password").passwordAgain("password").build();
        given(jwtGenerator.generateToken(any(UserEntity.class))).willReturn("123");

        LoginResponseDto loginResponseDto = userService.createUser(userDtoIn);

        then(userRepository).should().existsByUsername(userDtoIn.getUsername());
        then(userMapper).should().toEntity(userDtoIn);
        then(userRepository).should().save(any(UserEntity.class));
        assertEquals("123", loginResponseDto.getToken());
    }

    @Test
    void createUser_whenUsernameAlreadyExists_throwsException() {
        UserDtoIn userDtoIn = UserDtoIn.builder().username("abcde").build();
        given(userRepository.existsByUsername("abcde")).willReturn(Boolean.TRUE);

        Throwable thrown = catchThrowable(() -> userService.createUser(userDtoIn));

        then(userRepository).should().existsByUsername(userDtoIn.getUsername());
        assertThat(thrown).isInstanceOf(NameAlreadyExistsException.class).hasMessage("Username already exists");
    }

    @Test
    void createUser_whenPasswordsDoNotMatch_throwsException() {
        UserDtoIn userDtoIn = UserDtoIn.builder().password("abcde").passwordAgain("bdcef").build();

        Throwable thrown = catchThrowable(() -> userService.createUser(userDtoIn));

        then(userRepository).should().existsByUsername(userDtoIn.getUsername());
        assertThat(thrown).isInstanceOf(IncorrectInputException.class).hasMessage("Password fields do not match");
    }

    @Test
    void login_whenSuccessful_returnsCorrectResponse() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("testUser").password("password").build();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPassword");

        given(userRepository.existsByUsername("testUser")).willReturn(true);
        given(userRepository.findByUsername("testUser")).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches("password", "encodedPassword")).willReturn(true);
        given(jwtGenerator.generateToken(userEntity)).willReturn("token123");

        LoginResponseDto response = userService.login(loginRequestDto);

        then(userRepository).should().existsByUsername(loginRequestDto.getUsername());
        then(userRepository).should().findByUsername(loginRequestDto.getUsername());
        assertEquals("token123", response.getToken());
    }

    @Test
    void login_whenUsernameNotFound_throwsException() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("nonExistentUser").password("password").build();

        given(userRepository.existsByUsername("nonExistentUser")).willReturn(false);

        Throwable thrown = catchThrowable(() -> userService.login(loginRequestDto));

        then(userRepository).should().existsByUsername(loginRequestDto.getUsername());
        assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessage("Username doesn't exist");
    }

    @Test
    void login_whenPasswordIsIncorrect_throwsException() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("testUser").password("wrongPassword").build();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("encodedPassword");

        given(userRepository.existsByUsername("testUser")).willReturn(true);
        given(userRepository.findByUsername("testUser")).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches("wrongPassword", "encodedPassword")).willReturn(false);

        Throwable thrown = catchThrowable(() -> userService.login(loginRequestDto));

        then(userRepository).should().existsByUsername(loginRequestDto.getUsername());
        then(userRepository).should().findByUsername(loginRequestDto.getUsername());
        assertThat(thrown).isInstanceOf(IncorrectInputException.class).hasMessage("Invalid password");
    }

    @Test
    void findUser_whenUserFound_returnsCorrectUserDtoOut() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setUsername("John");
        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));

        UserDtoOut result = userService.findUser(1L);

        then(userMapper).should().toDto(userEntity);
        then(userRepository).should().findById(1L);
        assertEquals(1L, result.getUserId());
        assertEquals("John", result.getUsername());
    }

    @Test
    void findUser_whenUserNotFound_throwsNotFoundException() {
        Throwable thrown = catchThrowable(() -> userService.findUser(1L));

        then(userRepository).should().findById(1L);
        assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessage("User does not exist");
    }

    @Test
    void updateUserUsername_whenSuccessful_returnsUpdatedUserDtoOut() {
        UserDtoInUsername userDtoInUsername = UserDtoInUsername.builder().username("newUsername").build();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setUsername("oldUsername");

        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));
        given(userRepository.existsByUsername("newUsername")).willReturn(false);

        UserDtoOut result = userService.updateUserUsername(1L, userDtoInUsername);

        then(userRepository).should().findById(1L);
        then(userRepository).should().existsByUsername(userDtoInUsername.getUsername());
        then(userRepository).should().save(userEntity);
        assertEquals("newUsername", result.getUsername());
    }

    @Test
    void updateUserUsername_whenUserDoesNotExist_throwsException() {
        UserDtoInUsername userDtoInUsername = UserDtoInUsername.builder().build();

        Throwable thrown = catchThrowable(() -> userService.updateUserUsername(1L, userDtoInUsername));

        then(userRepository).should().findById(1L);
        assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessage("Cannot update a user that does not exist");
    }

    @Test
    void updateUserUsername_whenUsernameAlreadyExists_throwsException() {
        UserDtoInUsername userDtoInUsername = UserDtoInUsername.builder().username("existingUsername").build();

        given(userRepository.findById(1L)).willReturn(Optional.of(new UserEntity()));
        given(userRepository.existsByUsername("existingUsername")).willReturn(true);

        Throwable thrown = catchThrowable(() -> userService.updateUserUsername(1L, userDtoInUsername));

        then(userRepository).should().findById(1L);
        then(userRepository).should().existsByUsername(userDtoInUsername.getUsername());
        assertThat(thrown).isInstanceOf(NameAlreadyExistsException.class).hasMessage("Username already exists");
    }

    @Test
    void updateUserEmail_whenSuccessful_returnsUpdatedUserDtoOut() {
        UserDtoInEmail userDtoInEmail = UserDtoInEmail.builder().email("newemail@example.com").build();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setEmail("oldemail@example.com");

        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));

        UserDtoOut result = userService.updateUserEmail(1L, userDtoInEmail);

        then(userRepository).should().findById(1L);
        then(userRepository).should().save(userEntity);
        then(userMapper).should().toDto(userEntity);
        assertEquals("newemail@example.com", result.getEmail());
    }

    @Test
    void updateUserEmail_whenUserDoesNotExist_throwsException() {
        UserDtoInEmail userDtoInEmail = UserDtoInEmail.builder().build();

        Throwable thrown = catchThrowable(() -> userService.updateUserEmail(1L, userDtoInEmail));

        then(userRepository).should().findById(1L);
        assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessage("Cannot update a user that does not exist");
    }

    @Test
    void updateUserPassword_whenSuccessful_returnsUpdatedUserDtoOut() {
        UserPasswordChangeWrapper userPasswordChangeWrapper = UserPasswordChangeWrapper.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .confirmNewPassword("newPassword")
                .build();
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("encodedOldPassword");

        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches("oldPassword", "encodedOldPassword")).willReturn(true);

        userService.updateUserPassword(1L, userPasswordChangeWrapper);

        then(userRepository).should().findById(1L);
        then(passwordEncoder).should().matches("oldPassword", "encodedOldPassword");
        then(userRepository).should().save(userEntity);
        then(userMapper).should().toDto(userEntity);
    }

    @Test
    void updateUserPassword_whenUserDoesNotExist_throwsException() {
        UserPasswordChangeWrapper userPasswordChangeWrapper = UserPasswordChangeWrapper.builder().build();

        Throwable thrown = catchThrowable(() -> userService.updateUserPassword(1L, userPasswordChangeWrapper));

        then(userRepository).should().findById(1L);
        assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessage("Cannot update a user that does not exist");
    }

    @Test
    void updateUserPassword_whenOldPasswordIsIncorrect_throwsException() {
        UserPasswordChangeWrapper userPasswordChangeWrapper = UserPasswordChangeWrapper.builder()
                .oldPassword("wrongPassword")
                .newPassword("newPassword")
                .confirmNewPassword("newPassword")
                .build();
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("encodedOldPassword");

        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches("wrongPassword", "encodedOldPassword")).willReturn(false);

        Throwable thrown = catchThrowable(() -> userService.updateUserPassword(1L, userPasswordChangeWrapper));

        then(userRepository).should().findById(1L);
        then(passwordEncoder).should().matches("wrongPassword", "encodedOldPassword");
        assertThat(thrown).isInstanceOf(IncorrectInputException.class).hasMessage("Old password is incorrect");
    }

    @Test
    void updateUserPassword_whenNewPasswordsDoNotMatch_throwsException() {
        UserPasswordChangeWrapper userPasswordChangeWrapper = UserPasswordChangeWrapper.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .confirmNewPassword("differentNewPassword")
                .build();
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("encodedOldPassword");

        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches("oldPassword", "encodedOldPassword")).willReturn(true);

        Throwable thrown = catchThrowable(() -> userService.updateUserPassword(1L, userPasswordChangeWrapper));

        then(userRepository).should().findById(1L);
        assertThat(thrown).isInstanceOf(IncorrectInputException.class).hasMessage("New password fields do not match");
    }

    @Test
    void deleteUser_whenSuccessful_deletesUserAndAssociatedData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(userEntity));

        userService.deleteUser(1L);

        then(userRepository).should().findById(1L);
        then(wishListService).should().deleteByUserId(1L);
        then(userPreferencesService).should().deleteByUserId(1L);
        then(userRepository).should().delete(userEntity);
    }

    @Test
    void deleteUser_whenUserNotFound_throwsException() {
        Throwable thrown = catchThrowable(() -> userService.deleteUser(1L));

        then(userRepository).should().findById(1L);
        assertThat(thrown).isInstanceOf(NotFoundException.class).hasMessage("Cannot delete a user that does not exist");
    }
}
