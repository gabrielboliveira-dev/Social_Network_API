package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.FindUserByIdUseCase;
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
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByIdUseCaseImpl findUserByIdUseCase;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).username("testuser").build();
    }

    @Test
    @DisplayName("Deve retornar um usuário quando encontrado pelo ID")
    void shouldReturnUserWhenFoundById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = findUserByIdUseCase.handle(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando o usuário não for encontrado pelo ID")
    void shouldReturnEmptyOptionalWhenUserNotFoundById() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = findUserByIdUseCase.handle(userId);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }
}
