-- Flyway Migration: Create posts table
-- Author: Percival Gebashe
-- Description: Adds the posts table linked to user accounts

CREATE TABLE IF NOT EXISTS public.posts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    summary TEXT,
    content TEXT,
    tags JSONB DEFAULT '{}'::jsonb,
    thumbnail_url TEXT,
    visibility VARCHAR(20) DEFAULT 'PUBLIC',
    likes INTEGER DEFAULT 0,
    views INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_posts_user_id
        FOREIGN KEY (user_id)
        REFERENCES public.user_accounts (id)
        ON DELETE CASCADE
);

-- Indexes for search performance
CREATE INDEX idx_posts_user_id ON public.posts(user_id);
CREATE INDEX idx_posts_slug ON public.posts(slug);
CREATE INDEX idx_posts_visibility ON public.posts(visibility);

-- Trigger for automatic updated_at timestamp
CREATE OR REPLACE FUNCTION update_posts_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_posts_updated_at
BEFORE UPDATE ON public.posts
FOR EACH ROW
EXECUTE FUNCTION update_posts_updated_at_column();