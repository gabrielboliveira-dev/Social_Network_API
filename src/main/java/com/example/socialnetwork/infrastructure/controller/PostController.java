package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.usecase.comment.DeleteCommentUseCase;
import com.example.socialnetwork.application.usecase.post.*;

import com.example.socialnetwork.application.service.FileStorageService;
import com.example.socialnetwork.application.service.UserService;
import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.infrastructure.dto.AddCommentRequest;
import com.example.socialnetwork.infrastructure.dto.CommentResponse;
import com.example.socialnetwork.infrastructure.dto.CreatePostRequest;
import com.example.socialnetwork.infrastructure.dto.ImageUploadResponse;
import com.example.socialnetwork.infrastructure.dto.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final CreatePostUseCase createPostUseCase;
    private final UpdatePostImageUrlUseCase updatePostImageUrlUseCase;
    private final FindPostByIdUseCase findPostByIdUseCase;
    private final FindAllPostsUseCase findAllPostsUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final LikePostUseCase likePostUseCase;
    private final UnlikePostUseCase unlikePostUseCase;
    private final AddCommentUseCase addCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;

    private final UserService userService;
    private final FileStorageService fileStorageService;


    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request, Authentication authentication) {
        User author = findUserOrThrow(authentication);
        var command = new CreatePostUseCase.CreatePostCommand(request.getContent(), author);
        Post newPost = createPostUseCase.handle(command);
        return new ResponseEntity<>(convertToDto(newPost), HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/uploadImage")
    public ResponseEntity<ImageUploadResponse> uploadPostImage(@PathVariable UUID postId, @RequestParam("file") MultipartFile file, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        try {
            String filename = fileStorageService.save(file);
            var command = new UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand(postId, filename, currentUser);
            updatePostImageUrlUseCase.handle(command);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/")
                    .path(filename)
                    .toUriString();

            ImageUploadResponse response = new ImageUploadResponse();
            response.setFileName(filename);
            response.setFileDownloadUri(fileDownloadUri);
            response.setFileType(file.getContentType());
            response.setSize(file.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id) {
        return findPostByIdUseCase.handle(id)
                .map(this::convertToDto)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = findAllPostsUseCase.handle();
        List<PostResponse> postDtos = posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);
        try {
            var command = new DeletePostUseCase.DeletePostCommand(id, currentUser);
            deletePostUseCase.handle(command);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);
        try {
            var command = new LikePostUseCase.LikePostCommand(postId, currentUser);
            likePostUseCase.handle(command);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);
        try {
            var command = new UnlikePostUseCase.UnlikePostCommand(postId, currentUser);
            unlikePostUseCase.handle(command);
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
        User author = findUserOrThrow(authentication);
        try {
            var command = new AddCommentUseCase.AddCommentCommand(postId, request.getContent(), author);
            Comment newComment = addCommentUseCase.handle(command);
            return new ResponseEntity<>(convertToDto(newComment), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);
        try {
            var command = new DeleteCommentUseCase.DeleteCommentCommand(commentId, currentUser);
            deleteCommentUseCase.handle(command);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private User findUserOrThrow(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database."));
    }

    private PostResponse convertToDto(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setAuthor(post.getAuthor());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    private CommentResponse convertToDto(Comment comment) {
        CommentResponse dto = new CommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getAuthor());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}