package com.example.sns.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateDTO {

        private String comment;

        @Builder
        public CreateDTO(String comment) {
            this.comment = comment;
        }
    }
}
