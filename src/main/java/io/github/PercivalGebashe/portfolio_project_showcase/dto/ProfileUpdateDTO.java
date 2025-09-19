package io.github.PercivalGebashe.portfolio_project_showcase.dto;


public record ProfileUpdateDTO(Integer profileId,
                               String firstName,
                               String lastName,
                               String tagline,
                               String bio,
                               String skills,
                               String profilePictureUrl,
                               String contactLinks) {}