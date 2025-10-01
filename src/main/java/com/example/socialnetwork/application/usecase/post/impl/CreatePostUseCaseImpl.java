package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.CreatePostUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePostUseCaseImpl implements CreatePostUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post handle(CreatePostCommand command) {
        var newPost = Post.builder()
                .content(command.content())
                .author(command.author())
                .build();
        return postRepository.save(newPost);
    }
}