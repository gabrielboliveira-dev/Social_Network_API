package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.User;
import java.util.UUID;

public interface UpdatePostImageUrlUseCase {

    record UpdatePostImageUrlCommand(UUID postId, String imageUrl, User currentUser) {
    }

    void handle(UpdatePostImageUrlCommand command);
}