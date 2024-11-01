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
@SQLDelete(sql = "UPDATE like_entity SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

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
    private LikeEntity(User user, Post post) {
        this.assignUser(user);
        this.assignPost(post);
    }

    public static LikeEntity of(User user, Post post) {
        return LikeEntity.builder()
                .user(user)
                .post(post)
                .build();
    }

    // Like N <-> 1 User
    // 양방향 연관관계 편의 메서드
    public void assignUser(User user) {
        if(this.getUser() != null) {
            this.user.getLikeEntities().remove(this);
        }

        this.user = user;

        if(!user.getLikeEntities().contains(this)) {
            user.addLikeEntity(this);
        }
    }

    // Like N <-> 1 Post
    // 양방향 연관관계 편의 메서드
    public void assignPost(Post post) {
        if(this.getPost() != null) {
            this.post.getLikeEntities().remove(this);
        }

        this.post = post;

        if(!post.getLikeEntities().contains(this)) {
            post.addLikeEntity(this);
        }
    }
}
