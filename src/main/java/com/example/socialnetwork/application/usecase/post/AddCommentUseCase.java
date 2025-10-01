package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.User;
import java.util.UUID;

public interface AddCommentUseCase {

    record AddCommentCommand(UUID postId, String content, User author) {
    }

    Comment handle(AddCommentCommand command);
}