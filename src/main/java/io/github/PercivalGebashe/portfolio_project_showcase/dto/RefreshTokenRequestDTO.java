package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RefreshTokenRequestDTO{
    @NotBlank
    private String refreshToken;
}
