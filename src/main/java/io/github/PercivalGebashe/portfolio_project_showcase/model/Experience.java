package io.github.PercivalGebashe.portfolio_project_showcase.model;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ExperienceDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "experiences", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"userAccount"})
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Integer experienceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_experiences_user_id")
    )
    private UserAccount userAccount;

    @Column(name = "company", nullable = false)
    private String companyName;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "technologies", columnDefinition = "JSONB")
    private List<String> technologies = new ArrayList<>();

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current", nullable = false)
    private boolean isCurrent;

    public Experience(String companyName, String position, String description, List<String> technologies, LocalDate startDate, LocalDate endDate, boolean isCurrent) {
        this.companyName = companyName;
        this.position = position;
        this.description = description;
        this.technologies = technologies;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent;
    }

    public ExperienceDTO toDTO() {
        return new ExperienceDTO(
                companyName,
                position,
                description,
                technologies,
                startDate,
                endDate,
                isCurrent);
    }
}