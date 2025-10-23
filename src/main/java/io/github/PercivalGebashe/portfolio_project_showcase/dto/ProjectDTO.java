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
    private List<ContentItem> content;

    public static class ContentItem {
        private String type; // "paragraph" or "screenshot"
        private String value; // text or screenshot link
    }
}


