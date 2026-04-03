package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.LikePostUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikePostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private LikePostUseCaseImpl likePostUseCase;

    private UUID postId;
    private User currentUser;
    private Post post;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        currentUser = User.builder().id(UUID.randomUUID()).username("testuser").build();
        post = Post.builder().id(postId).content("Test content").build();
    }

    @Test
    @DisplayName("Deve adicionar um like ao post com sucesso")
    void shouldAddLikeToPostSuccessfully() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        LikePostUseCase.LikePostCommand command = new LikePostUseCase.LikePostCommand(postId, currentUser);
        likePostUseCase.handle(command);

        // Corrigido: Verificar se existe um Like com o currentUser
        assertTrue(post.getLikes().stream().anyMatch(like -> like.getUser().equals(currentUser)));
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o post não for encontrado")
    void shouldThrowExceptionWhenPostNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        LikePostUseCase.LikePostCommand command = new LikePostUseCase.LikePostCommand(postId, currentUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            likePostUseCase.handle(command);
        });

        assertEquals("Post not found.", exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("Não deve adicionar um like se o usuário já curtiu o post")
    void shouldNotAddLikeIfUserAlreadyLikedPost() {
        post.addLike(currentUser); // Adiciona o like antes do teste
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        int initialLikesCount = post.getLikes().size();

        LikePostUseCase.LikePostCommand command = new LikePostUseCase.LikePostCommand(postId, currentUser);
        likePostUseCase.handle(command);

        assertEquals(initialLikesCount, post.getLikes().size());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(post);
    }
}
