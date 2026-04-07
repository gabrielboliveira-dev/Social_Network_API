package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.usecase.user.RegisterUserUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.infrastructure.dto.LoginRequest;
import com.example.socialnetwork.infrastructure.dto.UserRegistrationRequest;
import com.example.socialnetwork.infrastructure.security.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("Deve registrar usuário e retornar 201 Created")
    void shouldRegisterUserSuccessfully() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("gabriel");
        request.setEmail("gabriel@example.com");
        request.setPassword("SenhaForte@123");

        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setUsername("gabriel");
        mockUser.setEmail("gabriel@example.com");

        when(registerUserUseCase.handle(any(RegisterUserUseCase.RegisterUserCommand.class))).thenReturn(mockUser);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("gabriel"))
                .andExpect(jsonPath("$.email").value("gabriel@example.com"));
    }

    @Test
    @DisplayName("Deve realizar login e retornar o Token JWT")
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("gabriel");
        request.setPassword("SenhaForte@123");

        Authentication mockAuth = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(mockAuth.getPrincipal()).thenReturn(mockUserDetails);
        when(jwtTokenUtil.generateToken(mockUserDetails)).thenReturn("fake-jwt-token-123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token-123"));
    }
}