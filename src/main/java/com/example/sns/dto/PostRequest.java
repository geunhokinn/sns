package com.example.sns.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateDTO {

        private String title;
        private String body;

        @Builder
        public CreateDTO(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateDTO {

        private String title;
        private String body;

        @Builder
        public UpdateDTO(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }
}
