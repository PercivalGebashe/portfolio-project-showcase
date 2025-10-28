package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectDTO {
    private String title;
    private String summary;
    private String repoLink;
    private String demoLink;
    private List<String> technologies;
    private List<ParagraphContent> content;
}


