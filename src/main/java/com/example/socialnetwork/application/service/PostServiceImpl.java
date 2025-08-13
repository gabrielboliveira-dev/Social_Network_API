package com.example.socialnetwork.application.service;

import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.Like;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.CommentRepository;
import com.example.socialnetwork.domain.repository.LikeRepository;
import com.example.socialnetwork.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Override
    public Post createPost(String content, User author) {
        Post post = Post.builder()
                .content(content)
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();
        return postRepository.save(post);
    }

    @Override
    public void updatePost(UUID postId, String content, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to update this post.");
        }
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Override
    public void deletePost(UUID id, User currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to delete this post.");
        }
        postRepository.delete(post);
    }

    @Override
    public void likePost(UUID postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        if (likeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new IllegalStateException("User has already liked this post.");
        }

        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
        likeRepository.save(like);
    }

    @Override
    public void unlikePost(UUID postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalStateException("User has not liked this post."));

        likeRepository.delete(like);
    }

    @Override
    public void addComment(UUID postId, String content, User author) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(UUID commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found."));

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to delete this comment.");
        }
        commentRepository.delete(comment);
    }
}