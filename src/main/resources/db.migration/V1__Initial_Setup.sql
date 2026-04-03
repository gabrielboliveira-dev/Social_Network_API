CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE posts (
                       id UUID PRIMARY KEY,
                       content TEXT NOT NULL,
                       image_url VARCHAR(255),
                       user_id UUID NOT NULL,
                       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP WITHOUT TIME ZONE,
                       CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE comments (
                          id UUID PRIMARY KEY,
                          content TEXT NOT NULL,
                          post_id UUID NOT NULL,
                          user_id UUID NOT NULL,
                          created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP WITHOUT TIME ZONE,
                          CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
                          CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE likes (
                       id UUID PRIMARY KEY,
                       post_id UUID NOT NULL,
                       user_id UUID NOT NULL,
                       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                       CONSTRAINT fk_likes_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
                       CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                       CONSTRAINT uk_post_user_like UNIQUE (post_id, user_id)
);