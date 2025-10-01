package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.FindPostByIdUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindPostByIdUseCaseImpl implements FindPostByIdUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> handle(UUID postId) {
        return postRepository.findById(postId);
    }
}