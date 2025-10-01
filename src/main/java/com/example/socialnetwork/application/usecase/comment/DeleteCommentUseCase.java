package com.example.socialnetwork.application.usecase.comment;

import com.example.socialnetwork.domain.entity.User;
import java.util.UUID;

public interface DeleteCommentUseCase {

    record DeleteCommentCommand(UUID commentId, User currentUser) {
    }

    void handle(DeleteCommentCommand command);
}