package com.example.sns.controller;

import com.example.sns.dto.AlarmResponse;
import com.example.sns.response.Response;
import com.example.sns.dto.UserRequest;
import com.example.sns.dto.UserResponse;
import com.example.sns.service.AlarmService;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AlarmService alarmService;

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

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse.ReadDTO>> getAlarmList(Pageable pageable, Authentication authentication) {

        return Response.success(userService.getAlarmList(pageable, authentication.getName()));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {

        return alarmService.connectAlarm(authentication.getName());
    }
}
