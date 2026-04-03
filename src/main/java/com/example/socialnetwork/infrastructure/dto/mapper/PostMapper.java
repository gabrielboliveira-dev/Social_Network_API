package com.example.socialnetwork.infrastructure.dto.mapper;

import com.example.socialnetwork.domain.entity.Comment;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.infrastructure.dto.CommentResponse;
import com.example.socialnetwork.infrastructure.dto.PostResponse;

import java.util.stream.Collectors;

public class PostMapper {

    public static PostResponse toDto(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        if (post.getAuthor() != null) {
            dto.setAuthorUsername(post.getAuthor().getUsername());
        }
        dto.setCreatedAt(post.getCreatedAt());
        if (post.getLikes() != null) {
            dto.setLikeCount(post.getLikes().size());
        }
        if (post.getComments() != null) {
            dto.setComments(post.getComments().stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList()));
        }
        return dto;
    }

    public static CommentResponse toDto(Comment comment) {
        CommentResponse dto = new CommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        if (comment.getAuthor() != null) {
            dto.setAuthorUsername(comment.getAuthor().getUsername());
        }
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
