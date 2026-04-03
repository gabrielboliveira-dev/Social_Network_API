package com.example.socialnetwork.application.usecase.comment.impl;

import com.example.socialnetwork.application.usecase.comment.AddCommentUseCase;
import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.CommentRepository;
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
class AddCommentUseCaseImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private AddCommentUseCaseImpl addCommentUseCase;

    private UUID postId;
    private User author;
    private String content;
    private Post post;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        author = User.builder()
                .id(UUID.randomUUID())
                .username("commenter")
                .email("commenter@example.com")
                .password("password")
                .build();
        content = "This is a test comment.";
        post = Post.builder()
                .id(postId)
                .content("Original post content")
                .author(User.builder().id(UUID.randomUUID()).username("poster").build())
                .build();
    }

    @Test
    @DisplayName("Deve adicionar um comentário a um post com sucesso")
    void shouldAddCommentToPostSuccessfully() {
        // Given
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddCommentUseCase.AddCommentCommand command =
                new AddCommentUseCase.AddCommentCommand(postId, content, author);

        // When
        Comment result = addCommentUseCase.handle(command);

        // Then
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(author, result.getAuthor()); // Corrigido de .getUser() para .getAuthor()
        assertEquals(post, result.getPost());
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o post não é encontrado")
    void shouldThrowExceptionWhenPostNotFound() {
        // Given
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        AddCommentUseCase.AddCommentCommand command =
                new AddCommentUseCase.AddCommentCommand(postId, content, author);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                addCommentUseCase.handle(command)
        );
        assertEquals("Post not found with ID: " + postId, exception.getMessage());
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, never()).save(any(Comment.class));
    }
}