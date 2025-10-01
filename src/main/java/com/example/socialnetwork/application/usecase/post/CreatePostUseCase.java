package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;

public interface CreatePostUseCase {

    record CreatePostCommand(String content, User author) {
    }

    Post handle(CreatePostCommand command);
}