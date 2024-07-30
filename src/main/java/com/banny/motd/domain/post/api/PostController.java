package com.banny.motd.domain.post.api;

import com.banny.motd.domain.post.api.dto.request.PostCreateRequest;
import com.banny.motd.domain.post.api.dto.request.PostModifyRequest;
import com.banny.motd.domain.post.api.dto.request.PostSearchRequest;
import com.banny.motd.domain.post.api.dto.response.PostCreateResponse;
import com.banny.motd.domain.post.api.dto.response.PostResponse;
import com.banny.motd.domain.post.application.PostService;
import com.banny.motd.domain.post.domain.Post;
import com.banny.motd.domain.user.domain.User;
import com.banny.motd.global.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<PostCreateResponse> createPost(@Valid @RequestBody PostCreateRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Post post = postService.createPost(request.getTitle(), request.getContent(), user.getId());

        return Response.success(PostCreateResponse.from(post));
    }

    @GetMapping
    public Response<List<PostResponse>> getPostList(PostSearchRequest request) {
        List<Post> posts = postService.getPostList(request);

        return Response.success(posts.stream().map(PostResponse::from).toList());
    }

    @GetMapping("/{id}")
    public Response<PostResponse> getPost(@PathVariable Long id) {
        Post post = postService.getPost(id);

        return Response.success(PostResponse.from(post));
    }

    @PutMapping("/{id}")
    public Response<Void> modifyPost(@PathVariable Long id, @RequestBody PostModifyRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        postService.modifyPost(id, request.getTitle(), request.getContent(), user.getId());

        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        postService.deletePost(id, user.getId());

        return Response.success();
    }
}
