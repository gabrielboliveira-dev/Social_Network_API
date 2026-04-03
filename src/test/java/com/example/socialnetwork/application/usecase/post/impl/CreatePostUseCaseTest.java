package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.CreatePostUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CreatePostUseCaseImpl createPostUseCase;

    @Test
    @DisplayName("Deve criar um post com sucesso quando os dados forem válidos")
    void shouldCreatePostSuccessfully() {
        User mockUser = new User();
        mockUser.setUsername("gabriel");

        CreatePostUseCase.CreatePostCommand command =
                new CreatePostUseCase.CreatePostCommand("Este é meu primeiro post!", mockUser);

        Post savedPost = new Post();
        savedPost.setContent("Este é meu primeiro post!");
        savedPost.setAuthor(mockUser);

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Post result = createPostUseCase.handle(command);

        assertNotNull(result);
        assertEquals("Este é meu primeiro post!", result.getContent());
        assertEquals("gabriel", result.getAuthor().getUsername());

        verify(postRepository, times(1)).save(any(Post.class));
    }
}