package com.example.socialnetwork.application.usecase.comment.impl;

import com.example.socialnetwork.application.usecase.comment.DeleteCommentUseCase;
import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.CommentRepository;
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
class DeleteCommentUseCaseImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private DeleteCommentUseCaseImpl deleteCommentUseCase;

    private UUID commentId;
    private User commentOwner;
    private User otherUser;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentId = UUID.randomUUID();
        commentOwner = User.builder()
                .id(UUID.randomUUID())
                .username("owner")
                .email("owner@example.com")
                .password("password")
                .build();
        otherUser = User.builder()
                .id(UUID.randomUUID())
                .username("other")
                .email("other@example.com")
                .password("password")
                .build();
        comment = Comment.builder()
                .id(commentId)
                .content("Test comment")
                .author(commentOwner)
                .build();
    }

    @Test
    @DisplayName("Deve excluir um comentário com sucesso pelo proprietário")
    void shouldDeleteCommentSuccessfully() {
        // Given
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        DeleteCommentUseCase.DeleteCommentCommand command =
                new DeleteCommentUseCase.DeleteCommentCommand(commentId, commentOwner);

        // When
        deleteCommentUseCase.handle(command);

        // Then
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o comentário não é encontrado")
    void shouldThrowExceptionWhenCommentNotFound() {
        // Given
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        DeleteCommentUseCase.DeleteCommentCommand command =
                new DeleteCommentUseCase.DeleteCommentCommand(commentId, commentOwner);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                deleteCommentUseCase.handle(command)
        );
        assertEquals("Comment not found.", exception.getMessage());
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não autorizado tenta excluir comentário")
    void shouldThrowExceptionWhenUnauthorizedUserDeletesComment() {
        // Given
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        DeleteCommentUseCase.DeleteCommentCommand command =
                new DeleteCommentUseCase.DeleteCommentCommand(commentId, otherUser);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                deleteCommentUseCase.handle(command)
        );
        assertEquals("User is not authorized to delete this comment.", exception.getMessage());
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).delete(any(Comment.class));
    }
}