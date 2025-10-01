package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.FindUserByIdUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindUserByIdUseCaseImpl implements FindUserByIdUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> handle(UUID userId) {
        return userRepository.findById(userId);
    }
}