package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.FindPostByIdUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
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
class FindPostByIdUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private FindPostByIdUseCaseImpl findPostByIdUseCase;

    private UUID postId;
    private Post post;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        post = Post.builder().id(postId).content("Test content").build();
    }

    @Test
    @DisplayName("Deve retornar um post quando encontrado pelo ID")
    void shouldReturnPostWhenFoundById() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Optional<Post> result = findPostByIdUseCase.handle(postId);

        assertTrue(result.isPresent());
        assertEquals(postId, result.get().getId());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando o post não for encontrado pelo ID")
    void shouldReturnEmptyOptionalWhenPostNotFoundById() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Optional<Post> result = findPostByIdUseCase.handle(postId);

        assertFalse(result.isPresent());
        verify(postRepository, times(1)).findById(postId);
    }
}
