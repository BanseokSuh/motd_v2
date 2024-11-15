package com.banny.motd.api.controller.post.response;

import com.banny.motd.domain.post.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailResponse {

    private Long id;
    private List<String> imageUrls;
    private String content;
    private LocalDateTime createdAt;
    private AuthorResponse author;
    private LikeResponse like;
    private CommentResponse comment;

    @Getter
    @Builder
    private static class AuthorResponse {
        private Long id;
        private String loginId;
        private String userName;
    }

    @Getter
    @Builder
    private static class LikeResponse {
        private int count;
        private List<LikeListResponse> list;
    }

    @Getter
    @Builder
    private static class LikeListResponse {
        private Long id;
        private LocalDateTime createdAt;
        private AuthorResponse author;
    }

    @Getter
    @Builder
    private static class CommentResponse {
        private int count;
        private List<CommentListResponse> list;
    }

    @Getter
    @Builder
    private static class CommentListResponse {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private AuthorResponse author;
    }

    public static PostDetailResponse from(PostDetail postDetail) {
        return new PostDetailResponse(
                postDetail.getId(),
                postDetail.getImageUrls(),
                postDetail.getContent(),
                postDetail.getCreatedAt(),
                AuthorResponse.builder() // 게시글 작성자
                        .id(postDetail.getAuthor().getId()) // 게시글 작성자 고유값
                        .loginId(postDetail.getAuthor().getLoginId()) // 게시글 작성자 아이디
                        .userName(postDetail.getAuthor().getUsername()) // 게시글 작성자 이름
                        .build(),
                LikeResponse.builder() // 좋아요
                        .count(postDetail.getLikeList().size()) // 좋아요 개수
                        .list(postDetail.getLikeList().stream() // 좋아요 목록
                                .map(postReaction -> LikeListResponse.builder()
                                        .id(postReaction.getId()) // 좋아요 식별값
                                        .createdAt(postReaction.getCreatedAt()) // 좋아요 생성일
                                        .author(AuthorResponse.builder() // 좋아요 작성자
                                                .id(postReaction.getAuthor().getId()) // 좋아요 작성자 식별값
                                                .loginId(postReaction.getAuthor().getLoginId()) // 좋아요 작성자 아이디
                                                .userName(postReaction.getAuthor().getUsername()) // 좋아요 작성자 이름
                                                .build())
                                        .build())
                                .toList())
                        .build(),
                CommentResponse.builder() // 댓글
                        .count(postDetail.getCommentList().size()) // 댓글 개수
                        .list(postDetail.getCommentList().stream() // 댓글 목록
                                .map(postComment -> CommentListResponse.builder()
                                        .id(postComment.getId()) // 댓글 식별값
                                        .content(postComment.getComment()) // 댓글 내용
                                        .createdAt(postComment.getCreatedAt()) // 댓글 생성일
                                        .author(AuthorResponse.builder() // 댓글 작성자
                                                .id(postComment.getAuthor().getId()) // 댓글 작성자 식별값
                                                .loginId(postComment.getAuthor().getLoginId()) // 댓글 작성자 아이디
                                                .userName(postComment.getAuthor().getUsername()) // 댓글 작성자 이름
                                                .build())
                                        .build())
                                .toList())
                        .build()
        );
    }

}
