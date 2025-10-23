-- Flyway Migration: Create experiences table linked to profiles
-- Author: Percival Gebashe

CREATE TABLE IF NOT EXISTS public.experiences (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    role VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_experience_user_id
        FOREIGN KEY (user_id)
        REFERENCES public.user_accounts (user_id)
        ON DELETE CASCADE
);