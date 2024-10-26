package com.example.sns.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JoinDTO {

        private String username;
        private String password;

        @Builder
        public JoinDTO(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginDTO {

        private String username;
        private String password;

        @Builder
        public LoginDTO(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
