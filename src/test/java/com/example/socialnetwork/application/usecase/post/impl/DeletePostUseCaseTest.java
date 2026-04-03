package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.DeletePostUseCase;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private DeletePostUseCaseImpl deletePostUseCase;

    private UUID postId;
    private User postAuthor;
    private User anotherUser;
    private Post post;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        postAuthor = User.builder().id(UUID.randomUUID()).username("author").build();
        anotherUser = User.builder().id(UUID.randomUUID()).username("anotheruser").build();
        post = Post.builder().id(postId).content("Test content").author(postAuthor).build();
    }

    @Test
    @DisplayName("Deve deletar um post com sucesso quando o usuário é o autor")
    void shouldDeletePostSuccessfullyWhenUserIsAuthor() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).delete(post);

        DeletePostUseCase.DeletePostCommand command = new DeletePostUseCase.DeletePostCommand(postId, postAuthor);
        deletePostUseCase.handle(command);

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o post não for encontrado")
    void shouldThrowExceptionWhenPostNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        DeletePostUseCase.DeletePostCommand command = new DeletePostUseCase.DeletePostCommand(postId, postAuthor);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deletePostUseCase.handle(command);
        });

        assertEquals("Post not found.", exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o usuário não é o autor do post")
    void shouldThrowExceptionWhenUserIsNotPostAuthor() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        
        DeletePostUseCase.DeletePostCommand command = new DeletePostUseCase.DeletePostCommand(postId, anotherUser);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deletePostUseCase.handle(command);
        });

        assertEquals("User is not authorized to perform this action on the post.", exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).delete(any(Post.class));
    }
}
