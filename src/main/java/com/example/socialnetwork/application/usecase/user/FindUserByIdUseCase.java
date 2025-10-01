package com.example.socialnetwork.application.usecase.user;

import com.example.socialnetwork.domain.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface FindUserByIdUseCase {

    Optional<User> handle(UUID userId);
}