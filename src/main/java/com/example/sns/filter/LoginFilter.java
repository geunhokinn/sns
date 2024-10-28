//package com.example.sns.filter;
//
//import com.example.sns.dto.auth.CustomUserDetails;
//import com.example.sns.dto.auth.LoginDTO;
//import com.example.sns.util.JWTUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletInputStream;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.util.StreamUtils;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Collection;
//import java.util.Iterator;
//
//@RequiredArgsConstructor
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//    private final JWTUtil jwtUtil;
//
//    // form login 을 disable 했기 때문에 직접 구현
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        LoginDTO loginDTO;
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            ServletInputStream inputStream = request.getInputStream();
//            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        String username = loginDTO.getUsername();
//        String password = loginDTO.getPassword();
//
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
//
//        return authenticationManager.authenticate(authToken);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) {
//
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        String username = customUserDetails.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();
//
//        String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L);
//
//        response.addHeader("Authorization", "Bearer " + token);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
//
//        response.setStatus(401);
//    }
//}