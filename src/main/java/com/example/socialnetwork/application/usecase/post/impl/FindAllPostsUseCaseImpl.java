package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.FindAllPostsUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllPostsUseCaseImpl implements FindAllPostsUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Post> handle() {
        return postRepository.findAll();
    }
}