package com.example.socialnetwork.application.usecase.user;

import com.example.socialnetwork.domain.entity.User;

public interface RegisterUserUseCase {

    record RegisterUserCommand(String username, String email, String plainTextPassword) {
    }

    User handle(RegisterUserCommand command);
}