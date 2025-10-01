package com.example.socialnetwork.application.usecase.user;

import com.example.socialnetwork.domain.entity.User;
import java.util.UUID;

public interface UpdateUserProfileImageUseCase {

    record UpdateUserProfileImageCommand(UUID userId, String imageUrl, User currentUser) {
    }

    void handle(UpdateUserProfileImageCommand command);
}