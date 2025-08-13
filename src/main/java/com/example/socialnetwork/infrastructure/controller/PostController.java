package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.service.PostService;
import com.example.socialnetwork.application.service.UserService;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.infrastructure.dto.CreatePostRequest;
import com.example.socialnetwork.infrastructure.dto.PostResponse;
import com.example.socialnetwork.infrastructure.dto.AddCommentRequest;
import com.example.socialnetwork.infrastructure.dto.CommentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> author = userService.findUserByUsername(userDetails.getUsername());

        if (author.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Post newPost = postService.createPost(request.getContent(), author.get());
        return new ResponseEntity<>(convertToPostDto(newPost), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id) {
        return postService.findPostById(id)
                .map(this::convertToPostDto)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.findAllPosts();
        List<PostResponse> postDtos = posts.stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> currentUser = userService.findUserByUsername(userDetails.getUsername());

        if (currentUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            postService.deletePost(id, currentUser.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userService.findUserByUsername(userDetails.getUsername());

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            postService.likePost(postId, user.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userService.findUserByUsername(userDetails.getUsername());

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            postService.unlikePost(postId, user.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentResponse> addComment(@PathVariable UUID postId,
                                                      @Valid @RequestBody AddCommentRequest request,
                                                      Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> author = userService.findUserByUsername(userDetails.getUsername());

        if (author.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Comment newComment = postService.addComment(postId, request.getContent(), author.get());
            return new ResponseEntity<>(convertToCommentDto(newComment), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> currentUser = userService.findUserByUsername(userDetails.getUsername());

        if (currentUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            postService.deleteComment(commentId, currentUser.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private PostResponse convertToPostDto(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setAuthor(post.getAuthor());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    private CommentResponse convertToCommentDto(Comment comment) {
        CommentResponse dto = new CommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getAuthor());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}