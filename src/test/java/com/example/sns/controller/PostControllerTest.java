package com.example.sns.controller;

import com.example.sns.dto.PostRequest;
import com.example.sns.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("포스트 작성 성공 테스트")
    @WithMockUser
    void createPostSuccess() throws Exception {

        String title = "title";
        String body = "body";
        String username = "testUser";

        doNothing().when(postService).createPost(title, body, username);

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                PostRequest.CreateDTO.builder()
                                        .title(title)
                                        .body(body)
                                        .build())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 작성 시 로그인하지 않은 경우 실패 테스트")
    @WithAnonymousUser
    void createPostFailure() throws Exception {

        String title = "title";
        String body = "body";

        // 로그인하지 않은 경우

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(
                                PostRequest.CreateDTO.builder()
                                        .title(title)
                                        .body(body)
                                        .build())))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
