package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String comment;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @Builder
    private Comment(User user, Post post, String comment) {
        this.assignUser(user);
        this.assignPost(post);
        this.comment = comment;
    }

    public static Comment of(User user, Post post, String comment) {
        return Comment.builder()
                .user(user)
                .post(post)
                .comment(comment)
                .build();
    }

    // Comment N <-> 1 User
    // 양방향 연관관계 편의 메서드
    public void assignUser(User user) {
        if(this.getUser() != null) {
            this.user.getComments().remove(this);
        }

        this.user = user;

        if(!user.getComments().contains(this)) {
            user.addComment(this);
        }
    }

    // Comment N <-> 1 Post
    // 양방향 연관관계 편의 메서드
    public void assignPost(Post post) {
        if(this.getPost() != null) {
            this.post.getComments().remove(this);
        }

        this.post = post;

        if(!post.getComments().contains(this)) {
            post.addComment(this);
        }
    }
}
