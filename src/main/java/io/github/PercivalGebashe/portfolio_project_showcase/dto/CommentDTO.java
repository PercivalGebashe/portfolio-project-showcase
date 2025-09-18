package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import java.time.LocalDateTime;

public record CommentDTO(
    Integer commentId,
    Integer projectId,
    Integer userId,
    Integer commenterUserId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){}
