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
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private final List<LikeEntity> likeEntities = new ArrayList<>();

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
    private Post(String title, String body, User user) {
        this.title = title;
        this.body = body;
        this.assignUser(user);
    }

    public static Post of(String title, String body, User user) {
        return Post.builder()
                .title(title)
                .body(body)
                .user(user)
                .build();
    }

    // Post N <-> 1 User
    // 양방향 연관관계 편의 메서드
    public void assignUser(User user) {
        if(this.getUser() != null) {
            this.user.getPosts().remove(this);
        }

        this.user = user;

        if(!user.getPosts().contains(this)) {
            user.addPost(this);
        }
    }

    // Post 1 <-> N Like
    // 양방향 연관관계 편의 메서드
    public void addLikeEntity(LikeEntity likeEntity) {
        this.likeEntities.add(likeEntity);

        if(likeEntity.getPost() != this) {
            likeEntity.assignPost(this);
        }
    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
