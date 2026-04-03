package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.UpdatePostUseCase;
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
class UpdatePostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private UpdatePostUseCaseImpl updatePostUseCase;

    private UUID postId;
    private User postAuthor;
    private User anotherUser;
    private Post post;
    private String newContent;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        postAuthor = User.builder().id(UUID.randomUUID()).username("author").build();
        anotherUser = User.builder().id(UUID.randomUUID()).username("anotheruser").build();
        post = Post.builder().id(postId).content("Old content").author(postAuthor).build();
        newContent = "New content for the post";
    }

    @Test
    @DisplayName("Deve atualizar o conteúdo do post com sucesso quando o usuário é o autor")
    void shouldUpdatePostContentSuccessfullyWhenUserIsAuthor() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        UpdatePostUseCase.UpdatePostCommand command = new UpdatePostUseCase.UpdatePostCommand(postId, newContent, postAuthor);
        updatePostUseCase.handle(command);

        assertEquals(newContent, post.getContent());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o post não for encontrado")
    void shouldThrowExceptionWhenPostNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        UpdatePostUseCase.UpdatePostCommand command = new UpdatePostUseCase.UpdatePostCommand(postId, newContent, postAuthor);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updatePostUseCase.handle(command);
        });

        assertEquals("Post not found.", exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o usuário não é o autor do post")
    void shouldThrowExceptionWhenUserIsNotPostAuthor() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        
        UpdatePostUseCase.UpdatePostCommand command = new UpdatePostUseCase.UpdatePostCommand(postId, newContent, anotherUser);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            updatePostUseCase.handle(command);
        });

        assertEquals("User is not authorized to perform this action on the post.", exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }
}
