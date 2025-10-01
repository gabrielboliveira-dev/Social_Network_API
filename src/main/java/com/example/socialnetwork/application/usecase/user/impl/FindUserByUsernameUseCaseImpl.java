package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.FindUserByUsernameUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindUserByUsernameUseCaseImpl implements FindUserByUsernameUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> handle(String username) {
        return userRepository.findByUsername(username);
    }
}