package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ParagraphContent {

    private String type; //type currently only paragraph
    private String text; // actuall paragrpahp body
    private String ImageUrl; // optional imagae URL
}
