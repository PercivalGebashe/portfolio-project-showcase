package io.github.PercivalGebashe.portfolio_project_showcase.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "profiles", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"userAccount"})
public class Profile {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @MapsId
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_profiles_user_id")
    )
    private UserAccount userAccount;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skills", columnDefinition = "JSONB")
    private Map<String, String> skills = new HashMap<>(); // JSON string: {"Java":"Intermediate","Python":"Beginner"}

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contact_links", columnDefinition = "JSONB")
    private Map<String, String> contactLinks = new HashMap<>(); // JSON string: {"github":"https://...","linkedin":"https://..."}

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

    public Map<String, String> getSkillsMap() {
        if (skills == null || skills.isEmpty()) return new HashMap<>();
        return skills;
    }
}