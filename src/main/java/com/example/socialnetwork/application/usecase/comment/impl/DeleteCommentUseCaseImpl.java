package com.example.socialnetwork.application.usecase.comment.impl;

import com.example.socialnetwork.application.usecase.comment.DeleteCommentUseCase;
import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCommentUseCaseImpl implements DeleteCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void handle(DeleteCommentCommand command) {
        Comment comment = commentRepository.findById(command.commentId())
                .orElseThrow(() -> new IllegalArgumentException("Comment not found."));
        comment.verifyOwnership(command.currentUser());
        commentRepository.delete(comment);
    }
}