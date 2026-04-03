package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.FindUserByUsernameUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByUsernameUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByUsernameUseCaseImpl findUserByUsernameUseCase;

    private String username;
    private User user;

    @BeforeEach
    void setUp() {
        username = "testuser";
        user = User.builder().id(UUID.randomUUID()).username(username).build();
    }

    @Test
    @DisplayName("Deve retornar um usuário quando encontrado pelo username")
    void shouldReturnUserWhenFoundByUsername() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = findUserByUsernameUseCase.handle(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando o usuário não for encontrado pelo username")
    void shouldReturnEmptyOptionalWhenUserNotFoundByUsername() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = findUserByUsernameUseCase.handle(username);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }
}
