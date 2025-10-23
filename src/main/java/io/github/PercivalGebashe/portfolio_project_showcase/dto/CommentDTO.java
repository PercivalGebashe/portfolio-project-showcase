package io.github.PercivalGebashe.portfolio_project_showcase.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentDTO{
    private Integer commentId;
    private Integer projectId;
    private Integer userId;
//    private Integer commenterUserId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
