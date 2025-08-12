package com.example.socialnetwork.application.service;

import com.example.socialnetwork.domain.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User registerUser(User user);
    Optional<User> findUserById(UUID id);
    Optional<User> findUserByUsername(String username);
}