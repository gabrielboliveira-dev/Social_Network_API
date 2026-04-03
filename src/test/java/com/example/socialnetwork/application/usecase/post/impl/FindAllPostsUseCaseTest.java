package com.example.socialnetwork.application.usecase.post.impl;

import com.example.socialnetwork.application.usecase.post.FindAllPostsUseCase;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllPostsUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private FindAllPostsUseCaseImpl findAllPostsUseCase;

    @Test
    @DisplayName("Deve retornar uma página de posts com sucesso")
    void shouldReturnPageOfPostsSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = Arrays.asList(
                Post.builder().content("Post 1").build(),
                Post.builder().content("Post 2").build()
        );
        Page<Post> expectedPage = new PageImpl<>(posts, pageable, posts.size());

        when(postRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Post> result = findAllPostsUseCase.handle(pageable);

        assertNotNull(result);
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(expectedPage.getContent().size(), result.getContent().size());
        assertEquals(expectedPage.getContent().get(0).getContent(), result.getContent().get(0).getContent());
        assertEquals(expectedPage.getContent().get(1).getContent(), result.getContent().get(1).getContent());

        verify(postRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar uma página vazia se não houver posts")
    void shouldReturnEmptyPageIfNoPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> expectedPage = Page.empty(pageable);

        when(postRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Post> result = findAllPostsUseCase.handle(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());

        verify(postRepository, times(1)).findAll(pageable);
    }
}
