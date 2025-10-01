package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.DeletePostUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletePostUseCaseImpl implements DeletePostUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void handle(DeletePostCommand command) {
        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
        post.verifyOwnership(command.currentUser());
        postRepository.delete(post);
    }
}