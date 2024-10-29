package com.example.sns.service;

import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.enumerate.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @Test
    @DisplayName("포스트 작성 성공 테스트")
    void createPostSuccess() {
        String title = "title";
        String body = "body";
        String username = "username";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        Assertions.assertDoesNotThrow(() -> postService.createPost(title, body, username));
    }

    @Test
    @DisplayName("포스트 작성 시 요청한 유저가 존재하지 않는 경우 실패 테스트")
    void createPostFailure() {
        String title = "title";
        String body = "body";
        String username = "username";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.createPost(title, body, username));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}
