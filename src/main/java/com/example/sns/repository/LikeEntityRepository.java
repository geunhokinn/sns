package com.example.sns.repository;

import com.example.sns.entity.LikeEntity;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserAndPost(User user, Post post);

    Long countByPost(Post post);

    @Modifying
    @Query("UPDATE LikeEntity l SET l.deletedAt = NOW() where l.post = :post")
    void deleteAllByPost(@Param("post") Post post);

//    void deleteAllByPost(Post post);
}
