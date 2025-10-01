package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.User;
import java.util.UUID;

public interface UpdatePostUseCase {

    record UpdatePostCommand(UUID postId, String content, User currentUser) {
    }

    void handle(UpdatePostCommand command);
}