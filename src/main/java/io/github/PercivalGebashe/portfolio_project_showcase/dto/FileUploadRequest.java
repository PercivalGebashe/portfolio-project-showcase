package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import jakarta.validation.constraints.NotBlank;

public record FileUploadRequest(
        @NotBlank(message = "userId cannot be blank.") String userId,
        @NotBlank(message = "name cannot be blank.")
        String name,
        @NotBlank(message = "URL cannot be black")
        String url,
        @NotBlank(message = "Size required")
        Long size) {}
