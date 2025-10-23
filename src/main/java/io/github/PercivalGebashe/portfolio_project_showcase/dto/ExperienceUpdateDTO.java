package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExperienceUpdateDTO {
    private String experiences;

    public List<ExperienceDTO> toDto() {
        if (experiences == null || experiences.isEmpty()) return Collections.emptyList();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return Arrays.asList(mapper.readValue(experiences, ExperienceDTO[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse experiences JSON", e);
        }
    }
}
