package com.example.socialnetwork.application.usecase.user.impl;

import com.example.socialnetwork.application.usecase.user.RegisterUserUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCaseImpl registerUserUseCase;

    private RegisterUserUseCase.RegisterUserCommand command;
    private User newUser;

    @BeforeEach
    void setUp() {
        command = new RegisterUserUseCase.RegisterUserCommand("testuser", "test@example.com", "password123");
        newUser = User.builder()
                .username(command.username())
                .email(command.email())
                .password("encodedPassword")
                .build();
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByUsername(command.username())).thenReturn(false);
        when(userRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode(command.plainTextPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = registerUserUseCase.handle(command);

        assertNotNull(result);
        assertEquals(command.username(), result.getUsername());
        assertEquals(command.email(), result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).existsByUsername(command.username());
        verify(userRepository, times(1)).existsByEmail(command.email());
        verify(passwordEncoder, times(1)).encode(command.plainTextPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o username já estiver em uso")
    void shouldThrowExceptionWhenUsernameAlreadyInUse() {
        when(userRepository.existsByUsername(command.username())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registerUserUseCase.handle(command);
        });

        assertEquals("Username or email already in use.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(command.username());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o email já estiver em uso")
    void shouldThrowExceptionWhenEmailAlreadyInUse() {
        when(userRepository.existsByUsername(command.username())).thenReturn(false);
        when(userRepository.existsByEmail(command.email())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registerUserUseCase.handle(command);
        });

        assertEquals("Username or email already in use.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(command.username());
        verify(userRepository, times(1)).existsByEmail(command.email());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
