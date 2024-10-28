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
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String role;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private final List<Post> posts = new ArrayList<>();

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @Builder
    private User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static User of(String username, String password, String role) {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

    // User 1 <-> N Post
    // 양방향 연관관계 편의 메서드
    public void addPost(Post post) {
        this.posts.add(post);

        if(post.getUser() != this) {
            post.assignUser(this);
        }
    }
}
