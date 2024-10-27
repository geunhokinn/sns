package com.example.sns.controller;

import com.example.sns.response.Response;
import com.example.sns.dto.UserRequest;
import com.example.sns.dto.UserResponse;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserResponse.JoinDTO> join(@RequestBody UserRequest.JoinDTO joinDTO) {
        UserResponse.JoinDTO joinResponse = userService.join(joinDTO.getUsername(), joinDTO.getPassword());
        return Response.success(joinResponse);
    }

    @PostMapping("/login")
    public Response<UserResponse.LoginDTO> login(@RequestBody UserRequest.LoginDTO loginDTO) {
        UserResponse.LoginDTO loginResponse = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return Response.success(loginResponse);
    }
}
