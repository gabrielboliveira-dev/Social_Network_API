package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.service.FileStorageService;
import com.example.socialnetwork.application.usecase.comment.AddCommentUseCase;
import com.example.socialnetwork.application.usecase.comment.DeleteCommentUseCase;
import com.example.socialnetwork.application.usecase.post.*;
import com.example.socialnetwork.application.usecase.user.FindUserByUsernameUseCase;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.infrastructure.security.JwtTokenUtil; // Import adicionado

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

@WebMvcTest(
        controllers = PostController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CreatePostUseCase createPostUseCase;
    @MockBean private UpdatePostImageUrlUseCase updatePostImageUrlUseCase;
    @MockBean private FindPostByIdUseCase findPostByIdUseCase;
    @MockBean private FindAllPostsUseCase findAllPostsUseCase;
    @MockBean private DeletePostUseCase deletePostUseCase;
    @MockBean private LikePostUseCase likePostUseCase;
    @MockBean private UnlikePostUseCase unlikePostUseCase;
    @MockBean private AddCommentUseCase addCommentUseCase;
    @MockBean private DeleteCommentUseCase deleteCommentUseCase;
    @MockBean private FindUserByUsernameUseCase findUserByUsernameUseCase;
    @MockBean private FileStorageService fileStorageService;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private JwtTokenUtil jwtTokenUtil;


    @Test
    @DisplayName("Deve retornar 400 Bad Request quando o conteúdo do post for vazio")
    @WithMockUser(username = "testuser")
    void shouldReturnBadRequestWhenPostContentIsEmpty() throws Exception {

        User mockUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .build();

        when(findUserByUsernameUseCase.handle(anyString()))
                .thenReturn(Optional.of(mockUser));

        String requestBody = """
                {
                    "content": ""
                }
                """;

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro na validação dos campos"))
                .andExpect(jsonPath("$.errors.content").exists());
    }
}