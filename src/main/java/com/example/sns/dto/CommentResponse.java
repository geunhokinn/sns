package com.example.sns.dto;

import com.example.sns.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class CommentResponse {

    @Getter
    @NoArgsConstructor
    public static class ReadDTO {

        private Long id;
        private String comment;
        private String username;
        private Long postId;
        private Timestamp registeredAt;
        private Timestamp updatedAt;
        private Timestamp deletedAt;

        @Builder
        private ReadDTO(Long id, String comment, String username, Long postId, Timestamp registeredAt, Timestamp updatedAt, Timestamp deletedAt) {
            this.id = id;
            this.comment = comment;
            this.username = username;
            this.postId = postId;
            this.registeredAt = registeredAt;
            this.updatedAt = updatedAt;
            this.deletedAt = deletedAt;
        }

        public static CommentResponse.ReadDTO from(Comment comment) {
            return ReadDTO.builder()
                    .id(comment.getId())
                    .comment(comment.getComment())
                    .username(comment.getUser().getUsername())
                    .postId(comment.getPost().getId())
                    .registeredAt(comment.getRegisteredAt())
                    .updatedAt(comment.getUpdatedAt())
                    .deletedAt(comment.getDeletedAt())
                    .build();
        }
    }
}
