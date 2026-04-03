package com.example.socialnetwork.application.usecase.comment;

import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.User;
import lombok.Value;

import java.util.UUID;

public interface AddCommentUseCase {

    Comment handle(AddCommentCommand command);

    @Value
    class AddCommentCommand {
        UUID postId;
        String content;
        User author;
    }
}
