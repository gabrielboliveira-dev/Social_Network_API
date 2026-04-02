package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllPostsUseCase {

    Page<Post> handle(Pageable pageable);
}