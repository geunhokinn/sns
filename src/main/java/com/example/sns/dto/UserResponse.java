package com.example.sns.dto;

import com.example.sns.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponse {

    @Getter
    @NoArgsConstructor
    public static class JoinDTO {

        private Long id;
        private String username;
        private String role;

        @Builder
        private JoinDTO(Long id, String username, String role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }

        public static JoinDTO from(User user) {
            return JoinDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LoginDTO {

        private String token;

        @Builder
        private LoginDTO(String token) {
            this.token = token;
        }
    }
}
