package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.UnlikePostUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnlikePostUseCaseImpl implements UnlikePostUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void handle(UnlikePostCommand command) {
        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
        post.removeLike(command.currentUser());
        postRepository.save(post);
    }
}