-- =========================================
-- V1__init_schema.sql
-- Production-ready baseline with indexes
-- =========================================

BEGIN;

-- 1. Roles table
CREATE TABLE roles (
    role_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_description VARCHAR(10) NOT NULL UNIQUE
);

-- 2. Email validation status table
CREATE TABLE email_validation_status (
    email_validation_status_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status_description VARCHAR(10) NOT NULL UNIQUE
);

-- 3. User accounts table
CREATE TABLE user_accounts (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role_id BIGINT NOT NULL,
    email_validation_status_id BIGINT NOT NULL,
    confirmation_token VARCHAR(100),
    confirmation_token_expiration TIMESTAMP,
    recovery_token VARCHAR(100),
    recovery_token_expiration TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles (role_id),
    CONSTRAINT fk_email_status FOREIGN KEY (email_validation_status_id) REFERENCES email_validation_status (email_validation_status_id)
);

-- Indexes for user_accounts
CREATE INDEX idx_user_role_id ON user_accounts(role_id);
CREATE INDEX idx_user_email_status_id ON user_accounts(email_validation_status_id);

-- 4. Profiles table
CREATE TABLE profiles (
    user_id BIGINT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    tagline VARCHAR(100),
    bio TEXT,
    profile_picture_url VARCHAR(300),
    skills JSONB,
    contact_links JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES user_accounts (user_id) ON DELETE CASCADE
);

-- 5. Projects table
CREATE TABLE projects (
    project_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    summary TEXT,
    project_image_url VARCHAR(300),
    content JSONB,
    technologies JSONB,
    repo_link VARCHAR(300),
    demo_link VARCHAR(300),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES user_accounts (user_id) ON DELETE CASCADE
);

-- Indexes for projects
CREATE INDEX idx_projects_user_id ON projects(user_id);

-- 6. Comments table
CREATE TABLE comments (
    comment_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_project FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES user_accounts (user_id) ON DELETE CASCADE
);

-- Indexes for comments
CREATE INDEX idx_comments_project_id ON comments(project_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);

-- 7. Likes table
CREATE TABLE likes (
    like_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_project FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE,
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES user_accounts (user_id) ON DELETE CASCADE,
    CONSTRAINT unique_project_user_like UNIQUE (project_id, user_id)
);

-- Indexes for likes
CREATE INDEX idx_likes_project_id ON likes(project_id);
CREATE INDEX idx_likes_user_id ON likes(user_id);

-- 8. Experiences table
CREATE TABLE experiences (
    experience_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    position VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    description TEXT,
    technologies JSONB,
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_experience_user_id FOREIGN KEY (user_id) REFERENCES user_accounts (user_id) ON DELETE CASCADE
);

-- Indexes for experiences
CREATE INDEX idx_experiences_user_id ON experiences(user_id);

COMMIT;
