package io.github.PercivalGebashe.portfolio_project_showcase.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperienceDTO {
    private Integer experienceId;
    private Integer userId;
    private String company;
    private String position;
    private String location;
    private String description;
    private List<String> technologies;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrent;

    public ExperienceDTO(String company, String position, String location, String description, List<String> technologies, LocalDate startDate, LocalDate endDate, boolean isCurrent) {
        this.company = company;
        this.position = position;
        this.location = location;
        this.description = description;
        this.technologies = technologies;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent;
    }
}
