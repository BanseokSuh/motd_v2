package com.lightcc.motd.domain.comment.api;

import com.lightcc.motd.domain.comment.api.dto.request.CommentCreateRequest;
import com.lightcc.motd.domain.comment.application.CommentService;
import com.lightcc.motd.domain.user.domain.User;
import com.lightcc.motd.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    public Response<Void> commentPost(@PathVariable Long postId, @RequestBody CommentCreateRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.commentPost(postId, user.getId(), request.getComment());

        return Response.success();
    }
}