package com.example.socialnetwork.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    public void addLike(User user) {
        boolean alreadyLiked = this.likes.stream()
                .anyMatch(like -> like.getUser().getId().equals(user.getId()));

        if (!alreadyLiked) {
            this.likes.add(new Like(this, user));
        }
    }

    public void removeLike(User user) {
        this.likes.removeIf(like -> like.getUser().getId().equals(user.getId()));
    }

    public Comment addComment(User author, String content) {
        Comment newComment = new Comment(this, author, content);
        this.comments.add(newComment);
        return newComment;
    }

    public void updateContent(String newContent, User currentUser) {
        verifyOwnership(currentUser);
        this.content = newContent;
    }

    public void verifyOwnership(User currentUser) {
        if (this.author == null || currentUser == null || !this.author.getId().equals(currentUser.getId())) {
            throw new IllegalStateException("User is not authorized to perform this action on the post.");
        }
    }

    public void updateImageUrl(String newImageUrl, User currentUser) {
        verifyOwnership(currentUser);
        this.imageUrl = newImageUrl;
    }
}