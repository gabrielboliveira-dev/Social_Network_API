package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.UnfollowUserUseCase;
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
class UnfollowUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UnfollowUserUseCaseImpl unfollowUserUseCase;

    private UUID currentUserId;
    private UUID userToUnfollowId;
    private User currentUser;
    private User userToUnfollow;

    @BeforeEach
    void setUp() {
        currentUserId = UUID.randomUUID();
        userToUnfollowId = UUID.randomUUID();
        currentUser = User.builder().id(currentUserId).username("currentUser").build();
        userToUnfollow = User.builder().id(userToUnfollowId).username("userToUnfollow").build();
    }

    @Test
    @DisplayName("Deve deixar de seguir um usuário com sucesso")
    void shouldUnfollowUserSuccessfully() {
        currentUser.follow(userToUnfollow);
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(userToUnfollowId)).thenReturn(Optional.of(userToUnfollow));
        when(userRepository.save(any(User.class))).thenReturn(currentUser).thenReturn(userToUnfollow);

        UnfollowUserUseCase.UnfollowUserCommand command = new UnfollowUserUseCase.UnfollowUserCommand(currentUserId, userToUnfollowId);
        unfollowUserUseCase.handle(command);

        assertFalse(currentUser.getFollowing().contains(userToUnfollow));
        assertFalse(userToUnfollow.getFollowers().contains(currentUser));
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, times(1)).findById(userToUnfollowId);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o usuário atual não for encontrado")
    void shouldThrowExceptionWhenCurrentUserNotFound() {
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        UnfollowUserUseCase.UnfollowUserCommand command = new UnfollowUserUseCase.UnfollowUserCommand(currentUserId, userToUnfollowId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            unfollowUserUseCase.handle(command);
        });

        assertEquals("Current user not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, never()).findById(userToUnfollowId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o usuário a deixar de seguir não for encontrado")
    void shouldThrowExceptionWhenUserToUnfollowNotFound() {
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(userToUnfollowId)).thenReturn(Optional.empty());

        UnfollowUserUseCase.UnfollowUserCommand command = new UnfollowUserUseCase.UnfollowUserCommand(currentUserId, userToUnfollowId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            unfollowUserUseCase.handle(command);
        });

        assertEquals("User to unfollow not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, times(1)).findById(userToUnfollowId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Não deve lançar exceção se o usuário não estiver seguindo o outro usuário")
    void shouldNotThrowExceptionIfUserIsNotFollowing() {
        // currentUser não segue userToUnfollow
        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(userToUnfollowId)).thenReturn(Optional.of(userToUnfollow));
        when(userRepository.save(any(User.class))).thenReturn(currentUser).thenReturn(userToUnfollow);

        UnfollowUserUseCase.UnfollowUserCommand command = new UnfollowUserUseCase.UnfollowUserCommand(currentUserId, userToUnfollowId);
        assertDoesNotThrow(() -> unfollowUserUseCase.handle(command));

        assertFalse(currentUser.getFollowing().contains(userToUnfollow));
        verify(userRepository, times(1)).findById(currentUserId);
        verify(userRepository, times(1)).findById(userToUnfollowId);
        verify(userRepository, times(2)).save(any(User.class));
    }
}
