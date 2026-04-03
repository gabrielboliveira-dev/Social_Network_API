package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.FollowUserUseCase;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowUserUseCaseImpl followUserUseCase;

    private UUID currentUserId;
    private UUID userToFollowId;
    private User currentUser;
    private User userToFollow;

    @BeforeEach
    void setUp() {
        currentUserId = UUID.randomUUID();
        userToFollowId = UUID.randomUUID();
        currentUser = User.builder().id(currentUserId).username("currentUser").build();
        userToFollow = User.builder().id(userToFollowId).username("userToFollow").build();
    }

    @Test
    @DisplayName("Deve seguir um usuário com sucesso")
    void shouldFollowUserSuccessfully() {
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(userToFollowId)).thenReturn(Optional.of(userToFollow));
        when(userRepository.save(any(User.class))).thenReturn(currentUser).thenReturn(userToFollow); // Mock multiple saves

        FollowUserUseCase.FollowUserCommand command = new FollowUserUseCase.FollowUserCommand(currentUserId, userToFollowId);
        followUserUseCase.handle(command);

        assertTrue(currentUser.getFollowing().contains(userToFollow));
        assertTrue(userToFollow.getFollowers().contains(currentUser));
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, times(1)).findById(userToFollowId);
        verify(userRepository, times(2)).save(any(User.class)); // One for currentUser, one for userToFollow
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando tentar seguir a si mesmo")
    void shouldThrowExceptionWhenFollowingSelf() {
        FollowUserUseCase.FollowUserCommand command = new FollowUserUseCase.FollowUserCommand(currentUserId, currentUserId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followUserUseCase.handle(command);
        });

        assertEquals("You cannot follow yourself.", exception.getMessage());
        verify(userRepository, never()).findById(any(UUID.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o usuário atual não for encontrado")
    void shouldThrowExceptionWhenCurrentUserNotFound() {
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        FollowUserUseCase.FollowUserCommand command = new FollowUserUseCase.FollowUserCommand(currentUserId, userToFollowId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followUserUseCase.handle(command);
        });

        assertEquals("Current user not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, never()).findById(userToFollowId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o usuário a ser seguido não for encontrado")
    void shouldThrowExceptionWhenUserToFollowNotFound() {
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(userToFollowId)).thenReturn(Optional.empty());

        FollowUserUseCase.FollowUserCommand command = new FollowUserUseCase.FollowUserCommand(currentUserId, userToFollowId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followUserUseCase.handle(command);
        });

        assertEquals("User to follow not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, times(1)).findById(userToFollowId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Não deve seguir um usuário se já estiver seguindo")
    void shouldNotFollowUserIfAlreadyFollowing() {
        currentUser.follow(userToFollow); // Já seguindo
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(userToFollowId)).thenReturn(Optional.of(userToFollow));
        when(userRepository.save(any(User.class))).thenReturn(currentUser).thenReturn(userToFollow);

        int initialFollowingCount = currentUser.getFollowing().size();

        FollowUserUseCase.FollowUserCommand command = new FollowUserUseCase.FollowUserCommand(currentUserId, userToFollowId);
        followUserUseCase.handle(command);

        assertEquals(initialFollowingCount, currentUser.getFollowing().size());
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, times(1)).findById(userToFollowId);
        verify(userRepository, times(2)).save(any(User.class));
    }
}
