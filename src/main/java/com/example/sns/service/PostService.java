package com.example.sns.service;

import com.example.sns.dto.PostResponse;
import com.example.sns.entity.LikeEntity;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.enumerate.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.repository.LikeEntityRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeEntityRepository likeEntityRepository;

    @Transactional
    public void createPost(String title, String body, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        postRepository.save(Post.of(title, body, user));
    }

    @Transactional
    public PostResponse.UpdateDTO updatePost(String title, String body, String username, Long postId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        if (post.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("s has no permission with %s", username, postId));
        }

        post.update(title, body);

        return PostResponse.UpdateDTO.from(postRepository.saveAndFlush(post));
    }

    @Transactional
    public void deletePost(String username, Long postId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        if (post.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("s has no permission with %s", username, postId));
        }

        postRepository.delete(post);
    }

    public Page<PostResponse.ReadDTO> readPosts(Pageable pageable) {

        return postRepository.findAll(pageable).map(PostResponse.ReadDTO::from);
    }

    public Page<PostResponse.ReadDTO> readMyPosts(String username, Pageable pageable) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

       return postRepository.findAllByUser(user, pageable).map(PostResponse.ReadDTO::from);
    }

    @Transactional
    public void likePost(Long postId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        likeEntityRepository.findByUserAndPost(user, post).ifPresent(existingLike -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("username %s already like post %d", username, postId));
        });

        likeEntityRepository.save(LikeEntity.of(user, post));
    }

    @Transactional
    public Long getLikeCountForPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        return likeEntityRepository.countByPost(post);
    }
}
