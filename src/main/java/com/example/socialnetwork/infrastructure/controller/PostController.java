package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.usecase.user.FindUserByUsernameUseCase;
import com.example.socialnetwork.application.usecase.comment.DeleteCommentUseCase;
import com.example.socialnetwork.application.usecase.comment.AddCommentUseCase;
import com.example.socialnetwork.application.usecase.post.CreatePostUseCase;
import com.example.socialnetwork.application.usecase.post.UpdatePostImageUrlUseCase;
import com.example.socialnetwork.application.usecase.post.FindPostByIdUseCase;
import com.example.socialnetwork.application.usecase.post.FindAllPostsUseCase;
import com.example.socialnetwork.application.usecase.post.DeletePostUseCase;
import com.example.socialnetwork.application.usecase.post.LikePostUseCase;
import com.example.socialnetwork.application.usecase.post.UnlikePostUseCase;

import com.example.socialnetwork.application.service.FileStorageService;
import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.infrastructure.dto.AddCommentRequest;
import com.example.socialnetwork.infrastructure.dto.CommentResponse;
import com.example.socialnetwork.infrastructure.dto.CreatePostRequest;
import com.example.socialnetwork.infrastructure.dto.ImageUploadResponse;
import com.example.socialnetwork.infrastructure.dto.PostResponse;
import com.example.socialnetwork.infrastructure.dto.mapper.PostMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
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

    private final FindUserByUsernameUseCase findUserByUsernameUseCase;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request, Authentication authentication) {
        User author = findUserOrThrow(authentication);
        var command = new CreatePostUseCase.CreatePostCommand(request.getContent(), author);
        Post newPost = createPostUseCase.handle(command);

        return new ResponseEntity<>(PostMapper.toDto(newPost), HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/uploadImage")
    public ResponseEntity<ImageUploadResponse> uploadPostImage(@PathVariable UUID postId, @RequestParam("file") MultipartFile file, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);
        
        String filename = fileStorageService.save(file);
        var command = new UpdatePostImageUrlUseCase.UpdatePostImageUrlCommand(postId, filename, currentUser);
        updatePostImageUrlUseCase.handle(command);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/images/")
                .path(filename)
                .toUriString();
        ImageUploadResponse response = new ImageUploadResponse();
        response.setFileName(filename);
        response.setFileDownloadUri(fileDownloadUri);
        response.setFileType(file.getContentType());
        response.setSize(file.getSize());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id) {
        return findPostByIdUseCase.handle(id)
                .map(PostMapper::toDto)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable) {
        Page<Post> posts = findAllPostsUseCase.handle(pageable);
        Page<PostResponse> postDtos = posts.map(PostMapper::toDto);

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        var command = new DeletePostUseCase.DeletePostCommand(id, currentUser);
        deletePostUseCase.handle(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        var command = new LikePostUseCase.LikePostCommand(postId, currentUser);
        likePostUseCase.handle(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        var command = new UnlikePostUseCase.UnlikePostCommand(postId, currentUser);
        unlikePostUseCase.handle(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentResponse> addComment(@PathVariable UUID postId,
                                                      @Valid @RequestBody AddCommentRequest request,
                                                      Authentication authentication) {
        User author = findUserOrThrow(authentication);

        var command = new AddCommentUseCase.AddCommentCommand(postId, request.getContent(), author);
        Comment newComment = addCommentUseCase.handle(command);

        return new ResponseEntity<>(PostMapper.toDto(newComment), HttpStatus.CREATED);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        var command = new DeleteCommentUseCase.DeleteCommentCommand(commentId, currentUser);
        deleteCommentUseCase.handle(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private User findUserOrThrow(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findUserByUsernameUseCase.handle(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database."));
    }
}
