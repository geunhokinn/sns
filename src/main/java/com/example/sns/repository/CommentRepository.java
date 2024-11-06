package com.example.sns.repository;

import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByPost(Post post, Pageable pageable);

    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = NOW() where c.post = :post")
    void deleteAllByPost(@Param("post") Post post);

//    void deleteAllByPost(Post post);

}
