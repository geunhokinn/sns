package com.example.sns.dto;

import com.example.sns.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class PostResponse {

    @Getter
    @NoArgsConstructor
    public static class UpdateDTO {

        private Long id;
        private String title;
        private String body;
        private UserResponse.JoinDTO userResponse;
        private Timestamp registeredAt;
        private Timestamp updatedAt;
        private Timestamp deletedAt;

        @Builder
        private UpdateDTO(Long id, String title, String body, UserResponse.JoinDTO userResponse, Timestamp registeredAt, Timestamp updatedAt, Timestamp deletedAt) {
            this.id = id;
            this.title = title;
            this.body = body;
            this.userResponse = userResponse;
            this.registeredAt = registeredAt;
            this.updatedAt = updatedAt;
            this.deletedAt = deletedAt;
        }

        public static PostResponse.UpdateDTO from(Post post) {
            return UpdateDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .body(post.getBody())
                    .userResponse(UserResponse.JoinDTO.from(post.getUser()))
                    .registeredAt(post.getRegisteredAt())
                    .updatedAt(post.getUpdatedAt())
                    .deletedAt(post.getDeletedAt())
                    .build();
        }
    }
}
