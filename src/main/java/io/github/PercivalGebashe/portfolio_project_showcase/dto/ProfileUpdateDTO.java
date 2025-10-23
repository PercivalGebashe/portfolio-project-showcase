package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProfileUpdateDTO{
    private Integer userId;
    private String firstName;
    private String lastName;
    private String tagline;
    private String bio;
    private String profilePictureUrl;
    private Map<String, String> skills = new HashMap<>();
    private Map<String, String> contactLinks = new HashMap<>();
}