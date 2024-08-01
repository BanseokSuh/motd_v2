package com.banny.motd.domain.post.application;

import com.banny.motd.domain.post.domain.PostAuthor;
import com.banny.motd.domain.user.domain.User;
import com.banny.motd.global.dto.request.SearchRequest;
import com.banny.motd.domain.post.domain.Post;
import com.banny.motd.domain.post.application.repository.PostRepository;
import com.banny.motd.domain.post.infrastructure.entity.PostEntity;
import com.banny.motd.domain.user.application.repository.UserRepository;
import com.banny.motd.domain.user.infrastructure.entity.UserEntity;
import com.banny.motd.global.exception.ApplicationException;
import com.banny.motd.global.exception.ResultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Post createPost(String title, String content, Long userId) {
        getUserEntityOrException(userId);

        return postRepository.save(PostEntity.of(title, content, userId)).toDomain();
    }

    @Override
    public List<PostAuthor> getPostList(SearchRequest request) {
        return postRepository.getPostListCustom(request)
                .stream()
                .map(postEntity -> PostAuthor.builder()
                        .post(postEntity.toDomain())
                        .user(getUserEntityOrException(postEntity.getUserId()).toDomain())
                        .build()).toList();
    }

    @Override
    public PostAuthor getPost(Long postId) {
        Post post = getPostEntityOrException(postId).toDomain();
        User user = getUserEntityOrException(post.getUserId()).toDomain();

        return PostAuthor.builder()
                .post(post)
                .user(user)
                .build();
    }

    @Override
    @Transactional
    public void modifyPost(Long postId, String title, String content, Long userId) {
        UserEntity userEntity = getUserEntityOrException(userId);
        PostEntity postEntity = getPostEntityOrException(postId);

        if (!postEntity.getUserId().equals(userEntity.getId())) {
            throw new ApplicationException(ResultType.INVALID_PERMISSION, String.format("UserId %s has no permission with PostId %s", userId, postId));
        }

        postEntity.setTitle(title);
        postEntity.setContent(content);

        postRepository.saveAndFlush(postEntity);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        UserEntity userEntity = getUserEntityOrException(userId);
        PostEntity postEntity = getPostEntityOrException(postId);

        if (!postEntity.getUserId().equals(userEntity.getId())) {
            throw new ApplicationException(ResultType.INVALID_PERMISSION, String.format("UserId %s has no permission with PostId %s", userId, postId));
        }

        postRepository.delete(postEntity);
    }

    public UserEntity getUserEntityOrException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ResultType.USER_NOT_FOUND, String.format("UserId %s is not found", userId)));
    }

    public PostEntity getPostEntityOrException(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ResultType.POST_NOT_FOUND, String.format("PostId %s is not found", postId)));
    }
}
