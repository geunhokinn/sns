package com.example.sns.filter;


import com.example.sns.dto.auth.CustomUserDetails;
import com.example.sns.dto.auth.InfoDTO;
import com.example.sns.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token;

        if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
            log.info("Request with {} check the query param", request.getRequestURI());
            token = request.getQueryString().split("=")[1].trim();
        } else {

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorization == null || !authorization.startsWith("Bearer ")) {

                log.info("Error occurs while getting header. header is null or invalid {}", request.getRequestURI());
                filterChain.doFilter(request, response);

                return;
            }

            log.info("authorization now");

            token = authorization.split(" ")[1];
        }

        if (jwtUtil.isExpired(token)) {

            log.info("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        InfoDTO infoDTO = InfoDTO.builder()
                .username(username)
                .password("password")
                .role(role)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(infoDTO);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
