package io.github.PercivalGebashe.portfolio_project_showcase.dto;

public record ProjectDTO(
    String title,
    ProjectDescriptionDTO description,
    String repoLink,
    String demoLink){}

