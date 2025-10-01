package com.example.socialnetwork.application.usecase.user;

import com.example.socialnetwork.domain.entity.User;
import java.util.Optional;

public interface FindUserByUsernameUseCase {

    Optional<User> handle(String username);
}