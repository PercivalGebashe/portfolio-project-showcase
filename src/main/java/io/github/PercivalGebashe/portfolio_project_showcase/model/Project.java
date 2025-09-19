package io.github.PercivalGebashe.portfolio_project_showcase.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProjectDescriptionDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "jsonb")
    private String descriptionJson;

    @Column(name = "repo_link")
    private String repoLink;

    @Column(name = "demo_link")
    private String demoLink;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the JSON description from a ProjectDescription object.
     */
    public void setDescription(ProjectDescriptionDTO description) {
        try {
            this.descriptionJson = objectMapper.writeValueAsString(description);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize project description", e);
        }
    }

    /**
     * Gets the description as a ProjectDescription object.
     */
    public ProjectDescriptionDTO getDescription() {
        try {
            return objectMapper.readValue(this.descriptionJson, ProjectDescriptionDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize project description", e);
        }
    }
}