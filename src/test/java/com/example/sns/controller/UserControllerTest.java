package com.example.sns.controller;

import com.example.sns.dto.UserRequest;
import com.example.sns.dto.UserResponse;
import com.example.sns.enumerate.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void joinSuccess() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.join(username, password)).thenReturn(mock(UserResponse.JoinDTO.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                UserRequest.JoinDTO.builder()
                                        .username(username)
                                        .password(password)
                                        .build()))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 시 이미 가입된 username 으로 가입하는 경우 에러 반환")
    public void joinFailure() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.join(username, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, null));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                UserRequest.JoinDTO.builder()
                                        .username(username)
                                        .password(password)
                                        .build()))
                ).andDo(print())
                .andExpect(status().isConflict());
    }


    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccess() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenReturn(mock(UserResponse.LoginDTO.class));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                UserRequest.LoginDTO.builder()
                                        .username(username)
                                        .password(password)
                                        .build()))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 시 회원가입인 안된 username 을 입력한 경우 에러반환")
    public void loginFailure1() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND, null));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                UserRequest.LoginDTO.builder()
                                        .username(username)
                                        .password(password)
                                        .build()))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 시 틀린 password 를 입력한 경우 에러반환")
    public void loginFailure2() throws Exception {
        String username = "username";
        String password = "password";

        when(userService.login(username, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD, null));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                UserRequest.LoginDTO.builder()
                                        .username(username)
                                        .password(password)
                                        .build()))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
