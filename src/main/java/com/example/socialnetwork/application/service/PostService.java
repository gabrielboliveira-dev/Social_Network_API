package com.example.socialnetwork.application.service;

import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
    Post createPost(String content, User author);
    void updatePost(UUID postId, String content, User currentUser);
    void deletePost(UUID id, User currentUser);
    void likePost(UUID postId, User user);
    void unlikePost(UUID postId, User user);
    Comment addComment(UUID postId, String content, User author);
    void deleteComment(UUID commentId, User currentUser);
    Optional<Post> findPostById(UUID id);
    List<Post> findAllPosts();
}