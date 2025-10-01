package com.example.socialnetwork.domain.repository;

import com.example.socialnetwork.domain.entity.Post;
import com.example.socialnetwork.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByAuthorOrderByCreatedAtDesc(User author);
}