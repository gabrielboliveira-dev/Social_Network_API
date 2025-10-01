package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.FollowUserUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowUserUseCaseImpl implements FollowUserUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void handle(FollowUserCommand command) {
        if (command.currentUserId().equals(command.userToFollowId())) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }
        User currentUser = userRepository.findById(command.currentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Current user not found."));

        User userToFollow = userRepository.findById(command.userToFollowId())
                .orElseThrow(() -> new IllegalArgumentException("User to follow not found."));
        currentUser.follow(userToFollow);

        userRepository.save(currentUser);
        userRepository.save(userToFollow);
    }
}