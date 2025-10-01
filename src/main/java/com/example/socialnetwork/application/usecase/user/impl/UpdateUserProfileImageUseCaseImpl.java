package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.UpdateUserProfileImageUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserProfileImageUseCaseImpl implements UpdateUserProfileImageUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void handle(UpdateUserProfileImageCommand command) {

        if (!command.userId().equals(command.currentUser().getId())) {
            throw new IllegalStateException("User is not authorized to update this profile image.");
        }

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.setProfileImageUrl(command.imageUrl());

        userRepository.save(user);
    }
}