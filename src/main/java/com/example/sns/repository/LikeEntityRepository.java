package com.example.sns.repository;

import com.example.sns.entity.LikeEntity;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserAndPost(User user, Post post);

    Long countByPost(Post post);
}
