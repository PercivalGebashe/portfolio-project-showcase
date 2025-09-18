package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import java.util.List;

public record ProjectParagraphDTO(
    String text,
    List<String> screenshots,
    List<String> videos
){}