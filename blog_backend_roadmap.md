# Portfolio Platform – Development Checklist  

## Core Setup
- [ ] Initialize Spring Boot project (Web, Security, JPA, Validation, Lombok, PostgreSQL/MySQL, JWT).  
- [ ] Configure `application.yml` (DB, JWT secrets, CORS).  
- [ ] Set up migrations (Flyway/Liquibase).  

## User & Profile
- [ ] Create **User entity** (id, email, password, roles).  
- [ ] Implement **Auth** (register, login, JWT).  
- [ ] Create **Profile entity** (bio, tagline, skills, contact links, image).  
- [ ] Implement **Profile CRUD** (update profile, get public profile).  
- [ ] Add **Contact form** → sends email to profile owner.  

## Projects
- [ ] Create **Project entity** (title, description, repo link, demo link, screenshots, tags, video).  
- [ ] Implement **CRUD endpoints** (create/update/delete project for owner).  
- [ ] Implement **Project listing** (as cards).  
- [ ] Implement **Project detail endpoint** (with media embeds).  

## Blog Posts (Optional)
- [ ] Create **Post entity** (title, content, createdAt).  
- [ ] Implement **CRUD endpoints** for posts.  

## Interactions
- [ ] Create **Comment entity** (linked to project or post).  
- [ ] Implement **Comment API** (add, list).  
- [ ] Create **Like entity** (toggle like on project or post).  

## Landing Page & Discovery
- [ ] Implement **public profiles listing** (mini card: image + tagline).  
- [ ] Add **search by name/skill/tag**.  
- [ ] Add **pagination & sorting**.  

## Admin & Moderation
- [ ] Build **Admin dashboard APIs** (suspend user, delete inappropriate projects/posts/comments).  
- [ ] Create **Report entity & endpoints** for content moderation.  

## Non-Functional
- [ ] Password hashing (BCrypt).  
- [ ] Input validation (size limits, file types).  
- [ ] Centralized error handling (`@ControllerAdvice`).  
- [ ] Swagger/OpenAPI docs.  
- [ ] Logging & monitoring.  
- [ ] Dockerfile + docker-compose (app + DB).  
- [ ] CI/CD setup (GitHub Actions/GitLab CI).