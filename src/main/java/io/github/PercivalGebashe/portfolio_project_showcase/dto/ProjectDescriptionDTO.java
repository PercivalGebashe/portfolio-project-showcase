package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectDescriptionDTO{
    private List<ProjectParagraphDTO> paragraphs;
}