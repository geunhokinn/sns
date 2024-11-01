package com.example.sns.controller;

import com.example.sns.dto.PostRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.response.Response;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@PathVariable Long postId, Authentication authentication) {

        postService.deletePost(authentication.getName(), postId);

        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse.ReadDTO>> readPosts(Pageable pageable, Authentication authentication) {

       return Response.success(postService.readPosts(pageable));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse.ReadDTO>> readMyPosts(Pageable pageable, Authentication authentication) {

        return Response.success(postService.readMyPosts(authentication.getName(), pageable));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> likePost(@PathVariable Long postId, Authentication authentication) {

        postService.likePost(postId, authentication.getName());

        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Long> getLikeCountForPost(@PathVariable Long postId, Authentication authentication) {

        return Response.success(postService.getLikeCountForPost(postId));
    }
}
