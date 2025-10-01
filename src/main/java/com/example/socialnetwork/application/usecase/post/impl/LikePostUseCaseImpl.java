package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.LikePostUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikePostUseCaseImpl implements LikePostUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void handle(LikePostCommand command) {
        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        post.addLike(command.currentUser());

        postRepository.save(post);
    }
}