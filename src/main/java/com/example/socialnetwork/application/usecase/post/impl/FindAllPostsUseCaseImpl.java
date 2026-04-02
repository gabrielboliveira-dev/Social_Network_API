package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.FindAllPostsUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindAllPostsUseCaseImpl implements FindAllPostsUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Post> handle(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}