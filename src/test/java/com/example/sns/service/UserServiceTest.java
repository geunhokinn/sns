package com.example.sns.service;

import com.example.sns.entity.User;
import com.example.sns.enumerate.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.fixture.UserFixture;
import com.example.sns.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("회원가입이 정상적으로 동작하는 경우")
    void joinSuccess() {
        String username = "username";
        String password = "password";
        User fixture = UserFixture.get(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encryptPassword");
        when(userRepository.save(any())).thenReturn(fixture);

        Assertions.assertDoesNotThrow(() -> userService.join(username, password));
    }

    @Test
    @DisplayName("회원가입 시 username 으로 회원가입한 유저가 이미 있는 경우")
    void joinFailure() {
        String username = "username";
        String password = "password";
        User fixture = UserFixture.get(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encryptPassword");
        when(userRepository.save(any())).thenReturn(fixture);

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(username, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인이 정상적으로 동작하는 경우")
    void loginSuccess() {
        String username = "username";
        String password = "password";
        User fixture = UserFixture.get(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(username, password));
    }

    @Test
    @DisplayName("로그인 시 username 으로 회원가입한 유저 없는 경우")
    void loginFailure1() {
        String username = "username";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(username, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인 시 password 가 틀린 경우")
    void loginFailure2() {
        String username = "username";
        String password = "password";
        String wrongPassword = "wrongPassword";
        User fixture = UserFixture.get(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.matches(wrongPassword, fixture.getPassword())).thenReturn(false);

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(username, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
