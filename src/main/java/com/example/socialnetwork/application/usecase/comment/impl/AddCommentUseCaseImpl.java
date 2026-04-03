package com.example.socialnetwork.application.usecase.comment.impl;

import com.example.socialnetwork.application.usecase.comment.AddCommentUseCase;
import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import com.example.socialnetwork.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddCommentUseCaseImpl implements AddCommentUseCase {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment handle(AddCommentCommand command) {
        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + command.getPostId()));

        Comment newComment = post.addComment(command.getAuthor(), command.getContent());
        return commentRepository.save(newComment);
    }
}
