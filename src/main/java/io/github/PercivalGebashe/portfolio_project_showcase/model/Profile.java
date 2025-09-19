package io.github.PercivalGebashe.portfolio_project_showcase.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "profiles", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Integer profileId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "bio")
    private String bio;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills; // JSON string: {"Java":"Intermediate","Python":"Beginner"}

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "contact_links", columnDefinition = "TEXT")
    private String contactLinks; // JSON string: {"github":"https://...","linkedin":"https://..."}

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // JSON helpers
    public Map<String, String> getSkillsMap() {
        if (skills == null || skills.isBlank()) return new HashMap<>();
        try {
            return new ObjectMapper().readValue(skills, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse skills JSON", e);
        }
    }

    public void setSkillsMap(Map<String, String> skillsMap) {
        try {
            this.skills = new ObjectMapper().writeValueAsString(skillsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize skills JSON", e);
        }
    }

    public Map<String, String> getContactLinksMap() {
        if (contactLinks == null || contactLinks.isBlank()) return new HashMap<>();
        try {
            return new ObjectMapper().readValue(contactLinks, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse contactLinks JSON", e);
        }
    }

    public void setContactLinksMap(Map<String, String> contactLinksMap) {
        try {
            this.contactLinks = new ObjectMapper().writeValueAsString(contactLinksMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize contactLinks JSON", e);
        }
    }
}