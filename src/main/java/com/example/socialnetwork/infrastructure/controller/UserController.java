package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.service.UserServiceImpl;
import com.example.socialnetwork.domain.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<Void> followUser(@PathVariable UUID followingId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> followerUser = userService.findUserByUsername(userDetails.getUsername());

        if (followerUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            userService.followUser(followerUser.get().getId(), followingId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/unfollow/{followingId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable UUID followingId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> followerUser = userService.findUserByUsername(userDetails.getUsername());

        if (followerUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            userService.unfollowUser(followerUser.get().getId(), followingId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}