package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.User;
import java.util.UUID;

public interface UnlikePostUseCase {

    record UnlikePostCommand(UUID postId, User currentUser) {
    }

    void handle(UnlikePostCommand command);
}