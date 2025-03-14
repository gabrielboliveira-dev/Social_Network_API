package com.example.socialnetwork.service;

import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Username already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }
        // TODO: Implement password encoding (Spring Security)
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // TODO: Implement logic for login (using Spring Security)
    public Optional<User> loginUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password)); // Basic check, replace with proper authentication
    }

    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        Optional<User> followerOptional = userRepository.findById(followerId);
        Optional<User> followingOptional = userRepository.findById(followingId);

        if (followerOptional.isPresent() && followingOptional.isPresent()) {
            User follower = followerOptional.get();
            User following = followingOptional.get();

            follower.getFollowing().add(following);
            following.getFollowers().add(follower);

            userRepository.save(follower);
            userRepository.save(following);
        } else {
            throw new IllegalArgumentException("Invalid user IDs");
        }
    }

    public void unfollowUser(Long followerId, Long followingId) {
        Optional<User> followerOptional = userRepository.findById(followerId);
        Optional<User> followingOptional = userRepository.findById(followingId);

        if (followerOptional.isPresent() && followingOptional.isPresent()) {
            User follower = followerOptional.get();
            User following = followingOptional.get();

            follower.getFollowing().remove(following);
            following.getFollowers().remove(follower);

            userRepository.save(follower);
            userRepository.save(following);
        } else {
            throw new IllegalArgumentException("Invalid user IDs");
        }
    }
}