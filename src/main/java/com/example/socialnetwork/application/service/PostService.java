package com.example.socialnetwork.application.service;

import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface PostService {
    Post createPost(String content, User author);
    Optional<Post> findPostById(UUID id);
    void deletePost(UUID id, User currentUser);
}