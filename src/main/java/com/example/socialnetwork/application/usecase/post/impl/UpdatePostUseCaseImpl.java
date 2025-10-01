package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.UpdatePostUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePostUseCaseImpl implements UpdatePostUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void handle(UpdatePostCommand command) {
        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
        post.updateContent(command.content(), command.currentUser());
        postRepository.save(post);
    }
}