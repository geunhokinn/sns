package com.example.sns.service;

import com.example.sns.dto.AlarmResponse;
import com.example.sns.dto.UserResponse;
import com.example.sns.dto.auth.InfoDTO;
import com.example.sns.entity.User;
import com.example.sns.enumerate.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.repository.AlarmRepository;
import com.example.sns.repository.UserCacheRepository;
import com.example.sns.repository.UserRepository;
import com.example.sns.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final UserCacheRepository userCacheRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    public InfoDTO loadUserByUsername(String username) throws UsernameNotFoundException {

        return userCacheRepository.getUser(username).orElseGet(() ->
                userRepository.findByUsername(username).map(InfoDTO::from).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username))));
    }

    @Transactional
    public UserResponse.JoinDTO join(String username, String password) {
        // 회원가입하려는 username 으로 회원가입된 user 가 있는지
        userRepository.findByUsername(username).ifPresent(existingUser -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", username));
        });

        // 회원가입 진행 = user 등록
        User user = userRepository.save(User.of(username, bCryptPasswordEncoder.encode(password), "ROLE_ADMIN"));

        return UserResponse.JoinDTO.from(user);
    }

    @Transactional
    public UserResponse.LoginDTO login(String username, String password) {
        // 회원가입 여부 체크
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        InfoDTO infoDTO = loadUserByUsername(username);
        userCacheRepository.setUser(infoDTO);

        // 비밀번호 체크
        if(!bCryptPasswordEncoder.matches(password, infoDTO.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD, null);
        }

        // 토큰 생성
        String token = jwtUtil.createJwt(infoDTO.getUsername(), infoDTO.getRole(), 60 * 60 * 10 * 1000L);

        return UserResponse.LoginDTO.builder()
                .token(token)
                .build();
    }

    public Page<AlarmResponse.ReadDTO> getAlarmList(Pageable pageable, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return alarmRepository.findAllByUser(user, pageable).map(AlarmResponse.ReadDTO::from);
    }
}
