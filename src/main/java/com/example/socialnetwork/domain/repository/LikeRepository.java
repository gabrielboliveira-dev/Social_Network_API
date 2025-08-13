package com.example.socialnetwork.domain.repository;

import com.example.socialnetwork.domain.entity.Like;
import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByUserAndPost(User user, Post post);
    int countByPost(Post post);
    void deleteByUserAndPost(User user, Post post);
}