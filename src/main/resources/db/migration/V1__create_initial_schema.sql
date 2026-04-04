-- V1__create_initial_schema.sql

-- Tabela de Usuários
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

-- Tabela de Posts
CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT NOT NULL,
    image_url VARCHAR(255),
    user_id UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Tabela de Likes
CREATE TABLE likes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    post_id UUID NOT NULL,
    liked_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_like_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT uk_user_post_like UNIQUE (user_id, post_id) -- Um usuário só pode dar like uma vez por post
);

-- Tabela de Comentários
CREATE TABLE comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT NOT NULL,
    user_id UUID NOT NULL,
    post_id UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);

-- Tabela de Seguidores (ManyToMany entre Usuários)
CREATE TABLE user_followers (
    user_id UUID NOT NULL,
    follower_id UUID NOT NULL,
    PRIMARY KEY (user_id, follower_id),
    CONSTRAINT fk_user_followers_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_followers_follower FOREIGN KEY (follower_id) REFERENCES users (id) ON DELETE CASCADE
);
