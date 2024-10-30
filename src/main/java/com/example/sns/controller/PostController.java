package com.example.sns.controller;

import com.example.sns.dto.PostRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.response.Response;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{postId}")
    public Response<PostResponse.UpdateDTO> updatePost(@PathVariable Long postId, @RequestBody PostRequest.UpdateDTO updateDTO, Authentication authentication) {

        PostResponse.UpdateDTO updateResponse = postService.updatePost(updateDTO.getTitle(), updateDTO.getBody(), authentication.getName(), postId);

        return Response.success(updateResponse);
    }
}
