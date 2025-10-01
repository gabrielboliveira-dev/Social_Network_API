package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.UnfollowUserUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnfollowUserUseCaseImpl implements UnfollowUserUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void handle(UnfollowUserCommand command) {
        User currentUser = userRepository.findById(command.currentUserId())
                .orElseThrow(() -> new IllegalArgumentException("Current user not found."));

        User userToUnfollow = userRepository.findById(command.userToUnfollowId())
                .orElseThrow(() -> new IllegalArgumentException("User to unfollow not found."));
        currentUser.unfollow(userToUnfollow);

        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);
    }
}