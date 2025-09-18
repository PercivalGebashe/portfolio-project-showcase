# 4-Week Roadmap – Portfolio Platform MVP

## Week 1 – Foundation
- [x] Initialize Spring Boot project (Web, Security, JPA, Lombok, DB).  
- [ ] Setup PostgreSQL/MySQL + migrations (Flyway/Liquibase).  
- [ ] Configure JWT-based authentication (register, login).  
- [ ] User entity (id, email, password, roles).  
- [ ] Password hashing (BCrypt).  
- [ ] Swagger/OpenAPI docs.  

## Week 2 – User Profiles
- [ ] Profile entity (bio, tagline, skills, profile picture).  
- [ ] Endpoints: create/update/get profile.  
- [ ] Public profile page (read-only).  
- [ ] Centralized error handling.  
- [ ] Dockerfile + DB setup (basic local run).  

## Week 3 – Projects
- [ ] Project entity (title, description, repo link, demo link).  
- [ ] CRUD endpoints (create/update/delete project for owner).  
- [ ] Public project listing (cards).  
- [ ] Project detail view (full description, screenshots, video link).  
- [ ] Likes (toggle) + Comments (basic).  

## Week 4 – Discovery & Polish
- [ ] Public landing page – user profile cards (mini picture + tagline).  
- [ ] Search/filter users (by name/skill).  
- [ ] Basic “Contact Me” (links + optional email form).  
- [ ] Test + refine API responses.  
- [ ] Finalize Swagger/OpenAPI docs.  
- [ ] Deploy to Render/Heroku/Dockerized VPS.

# Database Schema – Portfolio Platform (MVP)

## Entities

### 1. users
- **user_id** (PK) – UUID / BIGINT (auto)  
- **email** (unique, not null)  
- **password** (hashed, not null)  
- **role** (enum: USER, ADMIN)  
- **created_at** (timestamp)  
- **updated_at** (timestamp)  

---

### 2. profiles
- **profile_id** (PK) – UUID / BIGINT (auto)  
- **user_id** (FK → users.user_id, unique, not null)  
- **tagline** (varchar)  
- **bio** (text)  
- **skills** (json/text)  
- **profile_picture_url** (varchar)  
- **contact_links** (json/text: e.g., GitHub, LinkedIn, Website)  

---

### 3. projects
- **project_id** (PK) – UUID / BIGINT (auto)  
- **user_id** (FK → users.user_id, not null)  
- **title** (varchar, not null)  
- **description** (text)  
- **repo_link** (varchar)  
- **demo_link** (varchar)  
- **screenshot_urls** (json/text)  
- **video_url** (varchar)  
- **created_at** (timestamp)  
- **updated_at** (timestamp)  

---

### 4. comments
- **comment_id** (PK) – UUID / BIGINT (auto)  
- **project_id** (FK → projects.project_id, not null)  
- **user_id** (FK → users.user_id, not null)  
- **content** (text, not null)  
- **created_at** (timestamp)  

---

### 5. likes
- **like_id** (PK) – UUID / BIGINT (auto)  
- **project_id** (FK → projects.project_id, not null)  
- **user_id** (FK → users.user_id, not null)  
- **created_at** (timestamp)  
> Unique constraint: (project_id, user_id)

---

## Relationships
- **1 User → 1 Profile** (mandatory, one-to-one).  
- **1 User → Many Projects** (one-to-many).  
- **1 Project → Many Comments** (one-to-many).  
- **1 Project → Many Likes** (one-to-many).  
- **1 User → Many Comments** (one-to-many).  
- **1 User → Many Likes** (one-to-many).  

