package com.example.socialnetwork.application.service;

import com.example.socialnetwork.domain.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User registerUser(User user);
    void updateUserProfileImage(UUID userId, String imageUrl);
    Optional<User> findUserById(UUID id);
    Optional<User> findUserByUsername(String username);
    void followUser(UUID followerId, UUID followingId);
    void unfollowUser(UUID followerId, UUID followingId);
}