package com.example.socialnetwork.application.usecase.user;

import java.util.UUID;

public interface UnfollowUserUseCase {

    record UnfollowUserCommand(UUID currentUserId, UUID userToUnfollowId) {
    }

    void handle(UnfollowUserCommand command);
}