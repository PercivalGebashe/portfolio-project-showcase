package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank String refreshToken
) {}
