-- =========================================
-- V2__seed_roles_and_validation_status.sql
-- Seed data for roles & email validation status
-- =========================================

INSERT INTO roles (role_description)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN')
ON CONFLICT DO NOTHING;

INSERT INTO email_validation_status (status_description)
VALUES
    ('PENDING'),
    ('VERIFIED'),
    ('FAILED')
ON CONFLICT DO NOTHING;