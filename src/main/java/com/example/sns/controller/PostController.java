package com.example.sns.controller;

import com.example.sns.dto.PostRequest;
import com.example.sns.response.Response;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> createPost(@RequestBody PostRequest.CreateDTO createDTO, Authentication authentication) {

        postService.createPost(createDTO.getTitle(), createDTO.getBody(), authentication.getName());

        return Response.success();
    }
}
