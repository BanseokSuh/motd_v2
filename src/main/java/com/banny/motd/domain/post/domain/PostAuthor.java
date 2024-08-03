package com.banny.motd.domain.post.domain;

import com.banny.motd.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class PostAuthor {

    private Long id;
    private String title;
    private String content;
    private User author;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    @Builder
    public PostAuthor(Post post, User user) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.deletedAt = post.getDeletedAt();
        this.author = user;
    }
}