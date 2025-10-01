package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.UpdatePostImageUrlUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePostImageUrlUseCaseImpl implements UpdatePostImageUrlUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void handle(UpdatePostImageUrlCommand command) {
        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
        post.updateImageUrl(command.imageUrl(), command.currentUser());
        postRepository.save(post);
    }
}