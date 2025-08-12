package com.example.socialnetwork.application.service;

import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.socialnetwork.domain.repository.UserRepository;
import com.example.socialnetwork.application.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Username or email already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public void followUser(UUID followerId, UUID followingId) {
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

    public void unfollowUser(UUID followerId, UUID followingId) {
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