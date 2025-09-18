package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.CommentDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.mapper.CommentMapper;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Comment;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Project;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByProject(@PathVariable Integer projectId) {
        List<Comment> comments = commentRepository.findByProject_ProjectIdOrderByCreatedAtAsc(projectId);

        List<CommentDTO> commentDTOs = comments.stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOs);
    }

    @PostMapping("/project/{projectId}/user/{userId}")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Integer projectId,
            @PathVariable Integer userId,
            @RequestBody String content
    ) {

        Comment comment = new Comment();
        comment.setProject(new Project()); // set projectId
        comment.getProject().setProjectId(projectId);

        comment.setUserAccount(new UserAccount());
        comment.getUserAccount().setUserId(userId);

        comment.setContent(content);

        Comment saved = commentRepository.save(comment);
        return ResponseEntity.ok(CommentMapper.toDTO(saved));
    }
}
