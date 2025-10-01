package com.example.socialnetwork.application.usecase.user;

import java.util.UUID;

public interface FollowUserUseCase {

    record FollowUserCommand(UUID currentUserId, UUID userToFollowId) {
    }

    void handle(FollowUserCommand command);
}