package io.github.PercivalGebashe.portfolio_project_showcase.mapper;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.CommentDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Comment;

public class CommentMapper {
    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getCommentId(),
                comment.getProject().getProjectId(),
                comment.getUserAccount().getUserId(),
                comment.getCommenterId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
