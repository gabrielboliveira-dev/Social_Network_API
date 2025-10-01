package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.Post;
import java.util.Optional;
import java.util.UUID;

public interface FindPostByIdUseCase {

    Optional<Post> handle(UUID postId);
}