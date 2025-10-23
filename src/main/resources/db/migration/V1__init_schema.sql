-- =========================================
-- V1__init_schema.sql
-- Initial schema for portfolio_project_showcase
-- =========================================

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
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    email_validation_status_id BIGINT NOT NULL,
    confirmation_token VARCHAR(100),
    confirmation_token_expiration TIMESTAMP,
    recovery_token VARCHAR(100),
    recovery_token_expiration TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles (role_id),
    CONSTRAINT fk_email_status FOREIGN KEY (email_validation_status_id) REFERENCES email_validation_status (email_validation_status_id)
);

-- 4. Profiles table
CREATE TABLE profiles (
    profile_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    tagline VARCHAR(100),
    bio TEXT,
    skills JSONB,
    profile_picture_url VARCHAR(100),
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
    description TEXT,
    repo_link VARCHAR(100),
    demo_link VARCHAR(100),
    screenshot_urls JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES user_accounts (user_id) ON DELETE CASCADE
);

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