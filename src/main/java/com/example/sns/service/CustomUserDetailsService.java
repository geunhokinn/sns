//package com.example.sns.service;
//
//import com.example.sns.dto.auth.CustomUserDetails;
//import com.example.sns.dto.auth.InfoDTO;
//import com.example.sns.entity.User;
//import com.example.sns.enumerate.ErrorCode;
//import com.example.sns.exception.SnsApplicationException;
//import com.example.sns.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
//
//        return new CustomUserDetails(
//                InfoDTO.builder()
//                        .username(user.getUsername())
//                        .password(user.getPassword())
//                        .role(user.getRole())
//                        .build()
//        );
//    }
//}
