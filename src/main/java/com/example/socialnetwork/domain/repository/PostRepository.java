package com.example.socialnetwork.domain.repository;

import com.example.socialnetwork.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    // Métodos de busca específicos, se necessário, podem ser adicionados aqui no futuro.
}