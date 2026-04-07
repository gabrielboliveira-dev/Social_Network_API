package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.service.FileStorageService;
import com.example.socialnetwork.application.usecase.user.*;
import com.example.socialnetwork.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockBean
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    @MockBean
    private UpdateUserProfileImageUseCase updateUserProfileImageUseCase;

    @MockBean
    private FollowUserUseCase followUserUseCase;

    @MockBean
    private UnfollowUserUseCase unfollowUserUseCase;

    @MockBean
    private FileStorageService fileStorageService;

    private User mockCurrentUser;

    @BeforeEach
    void setUp() {
        mockCurrentUser = new User();
        mockCurrentUser.setId(UUID.randomUUID());
        mockCurrentUser.setUsername("testuser");

        when(findUserByUsernameUseCase.handle("testuser")).thenReturn(Optional.of(mockCurrentUser));
    }

    @Test
    @DisplayName("Deve buscar um usuário por ID e retornar 200 OK")
    @WithMockUser(username = "testuser")
    void shouldGetUserById() throws Exception {
        UUID targetUserId = UUID.randomUUID();
        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setUsername("maria");
        targetUser.setEmail("maria@example.com");

        when(findUserByIdUseCase.handle(targetUserId)).thenReturn(Optional.of(targetUser));

        mockMvc.perform(get("/api/v1/users/" + targetUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("maria"));
    }

    @Test
    @DisplayName("Deve fazer upload da imagem de perfil e retornar as URIs")
    @WithMockUser(username = "testuser")
    void shouldUploadProfileImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "foto.png", MediaType.IMAGE_PNG_VALUE, "imagem".getBytes()
        );

        when(fileStorageService.save(any())).thenReturn("foto_salva_123.png");

        mockMvc.perform(multipart("/api/v1/users/uploadProfileImage").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("foto_salva_123.png"))
                .andExpect(jsonPath("$.fileDownloadUri").exists());

        verify(updateUserProfileImageUseCase).handle(any(UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand.class));
    }

    @Test
    @DisplayName("Deve seguir um usuário e retornar 200 OK")
    @WithMockUser(username = "testuser")
    void shouldFollowUser() throws Exception {
        UUID followingId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/users/follow/" + followingId))
                .andExpect(status().isOk());

        verify(followUserUseCase).handle(any(FollowUserUseCase.FollowUserCommand.class));
    }

    @Test
    @DisplayName("Deve deixar de seguir um usuário e retornar 200 OK")
    @WithMockUser(username = "testuser")
    void shouldUnfollowUser() throws Exception {
        UUID followingId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/users/unfollow/" + followingId))
                .andExpect(status().isOk());

        verify(unfollowUserUseCase).handle(any(UnfollowUserUseCase.UnfollowUserCommand.class));
    }
}