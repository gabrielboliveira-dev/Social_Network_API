package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.RegisterUserUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User handle(RegisterUserCommand command) {
        if (userRepository.existsByUsername(command.username()) || userRepository.existsByEmail(command.email())) {
            throw new IllegalStateException("Username or email already in use.");
        }
        var user = User.builder()
                .username(command.username())
                .email(command.email())
                .password(passwordEncoder.encode(command.plainTextPassword()))
                .build();
        return userRepository.save(user);
    }
}