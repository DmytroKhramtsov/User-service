package com.company.userservice.service;

import com.company.userservice.entity.UserEntity;
import com.company.userservice.mapper.UserMapper;
import com.company.userservice.model.User;
import com.company.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles({"test"})
@MockitoSettings
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private UserService userService;

    @Test
    void createUserTest() {
        User user = User.builder()
                .id(0)
                .login("login-1")
                .password("password-1")
                .build();
        UserEntity userEntity = mapper.map(user);

        userRepository.save(userEntity);

        when(userRepository.save(mapper.map(user))).thenReturn(userEntity);

        verify(userRepository, times(1)).save(mapper.map(user));
        assertThat(userRepository.save(mapper.map(user))).isEqualTo(mapper.map(user));
    }

    @Test
    void updateUserTest() {
        User user = User.builder()
                .id(1)
                .login("login-1")
                .password("password-1")
                .build();
        User updatedUser = User.builder()
                .id(1)
                .login("login-2")
                .password("password-2")
                .build();
        UserEntity userEntity = mapper.map(user);
        UserEntity updatedUserEntity = mapper.map(updatedUser);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save((Mockito.any(UserEntity.class)))).thenReturn(updatedUserEntity);

        User result = userService.update(updatedUser, updatedUser.getId());

        Assertions.assertEquals(updatedUser.getLogin(), result.getLogin());
        Assertions.assertEquals(updatedUser.getPassword(), result.getPassword());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    void deleteByIdTest() {
        User user = User.builder()
                .id(0)
                .login("login-1")
                .password("password-1")
                .build();
        userRepository.deleteById(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void createTokenTest() {
        String expectedTokenStart = "login-1:password-1:";
        User user = User.builder()
                .id(0)
                .login("login-1")
                .password("password-1")
                .build();
        UserEntity userEntity = mapper.map(user);
        when(userRepository.findByLogin(eq("login-1"))).thenReturn(Optional.of(userEntity));

        String actualToken = userService.createToken(user);

        assertThat(actualToken).startsWith(expectedTokenStart);
        assertThat(actualToken.split(":")[2]).matches("^-?\\d{1,19}$");
    }

    @Test
    void createTokenWithWrongLoginTest() {
        User user = User.builder()
                .login("login-1")
                .password("password-1")
                .build();
        User userWrongLogin = User.builder()
                .login("wrong-login")
                .password("password-1")
                .build();

        assertThat(userService.createToken(userWrongLogin)).isEqualTo("User not found");
    }

    @Test
    void createTokenWithWrongPasswordTest() {
        User user = User.builder()
                .login("login-1")
                .password("password-1")
                .build();
        UserEntity userEntity = mapper.map(user);

        when(userRepository.findByLogin(eq("login-1"))).thenReturn(Optional.of(userEntity));
        User userWrongPass = User.builder()
                .login("login-1")
                .password("wrong-password")
                .build();

        assertThat(userService.createToken(userWrongPass)).isEqualTo("Wrong Password");
    }

    @Test
    void TokenIsValidTest() {
        User user = User.builder()
                .login("login-1")
                .password("password-1")
                .build();
        UserEntity userEntity = mapper.map(user);

        when(userRepository.findByLogin(eq("login-1"))).thenReturn(Optional.of(userEntity));
        long now = Instant.now().toEpochMilli();
        String token = "login-1:password-1:" + now;
        assertThat(userService.tokenIsValid(token)).isEqualTo(true);
        String tokenWrong = "login-2:password-2:" + now;
        assertThat(userService.tokenIsValid(tokenWrong)).isEqualTo(false);
    }
}
