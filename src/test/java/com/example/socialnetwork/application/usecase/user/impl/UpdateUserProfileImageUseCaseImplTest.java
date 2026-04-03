package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.UpdateUserProfileImageUseCase;
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
class UpdateUserProfileImageUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserProfileImageUseCaseImpl updateUserProfileImageUseCase;

    private UUID userId;
    private User currentUser;
    private String newImageUrl;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        currentUser = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();
        newImageUrl = "http://example.com/new-image.jpg";
    }

    @Test
    @DisplayName("Deve atualizar a imagem de perfil do usuário com sucesso")
    void shouldUpdateUserProfileImageSuccessfully() {
        // Given
        User userToUpdate = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .profileImageUrl("http://example.com/old-image.jpg")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));

        UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand command =
                new UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand(userId, newImageUrl, currentUser);

        // When
        updateUserProfileImageUseCase.handle(command);

        // Then
        assertEquals(newImageUrl, userToUpdate.getProfileImageUrl());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(userToUpdate);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não autorizado tenta atualizar imagem de perfil")
    void shouldThrowExceptionWhenUnauthorizedUserUpdatesProfileImage() {
        // Given
        UUID anotherUserId = UUID.randomUUID();
        UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand command =
                new UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand(anotherUserId, newImageUrl, currentUser);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                updateUserProfileImageUseCase.handle(command)
        );
        assertEquals("User is not authorized to update this profile image.", exception.getMessage());
        verify(userRepository, never()).findById(any(UUID.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é encontrado")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand command =
                new UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand(userId, newImageUrl, currentUser);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                updateUserProfileImageUseCase.handle(command)
        );
        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}
