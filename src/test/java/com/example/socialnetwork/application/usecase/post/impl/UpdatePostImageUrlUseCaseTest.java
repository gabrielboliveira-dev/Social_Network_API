package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.UpdatePostImageUrlUseCase;
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
class UpdatePostImageUrlUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private UpdatePostImageUrlUseCaseImpl updatePostImageUrlUseCase;

    private UUID postId;
    private User postAuthor;
    private User anotherUser;
    private Post post;
    private String newImageUrl;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        postAuthor = User.builder().id(UUID.randomUUID()).username("author").build();
        anotherUser = User.builder().id(UUID.randomUUID()).username("anotheruser").build();
        post = Post.builder().id(postId).content("Test content").author(postAuthor).imageUrl("old_image.jpg").build();
        newImageUrl = "new_image.jpg";
    }

    @Test
    @DisplayName("Deve atualizar a URL da imagem do post com sucesso quando o usuário é o autor")
    void shouldUpdatePostImageUrlSuccessfullyWhenUserIsAuthor() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand command =
                new UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand(postId, newImageUrl, postAuthor);
        updatePostImageUrlUseCase.handle(command);

        assertEquals(newImageUrl, post.getImageUrl());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o post não for encontrado")
    void shouldThrowExceptionWhenPostNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand command =
                new UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand(postId, newImageUrl, postAuthor);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updatePostImageUrlUseCase.handle(command);
        });

        assertEquals("Post not found.", exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o usuário não é o autor do post")
    void shouldThrowExceptionWhenUserIsNotPostAuthor() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        
        UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand command =
                new UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand(postId, newImageUrl, anotherUser);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> { // Corrigido para IllegalStateException
            updatePostImageUrlUseCase.handle(command);
        });

        assertEquals("User is not authorized to perform this action on the post.", exception.getMessage()); // Corrigido para a mensagem correta
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }
}
