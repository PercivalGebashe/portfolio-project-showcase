package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseDTO(
        boolean success,
        String message,
        Map<String, Object> data
) {}
