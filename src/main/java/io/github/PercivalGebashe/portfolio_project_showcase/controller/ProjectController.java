package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProjectDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Project;
import io.github.PercivalGebashe.portfolio_project_showcase.service.ProjectService;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.ApiResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // CREATE a project
    @PostMapping
    public ResponseEntity<ApiResponseDTO> createProject(
            @RequestParam Integer userId,
            @RequestBody ProjectDTO projectDTO
    ) {
        Project project = projectService.createProject(userId, projectDTO);
        return ResponseEntity.ok(new ApiResponseDTO(
                true,
                "Project created",
                Map.of("project",project)));
    }

    // GET project by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getProject(@PathVariable Integer id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(new ApiResponseDTO(true, "Project retrieved", Map.of(
                "project", project
        )));
    }

    // GET all projects for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO> getProjectsForUser(@PathVariable Integer userId) {
        List<Project> projects = projectService.getProjectsForUser(userId);
        return ResponseEntity.ok(new ApiResponseDTO(true,
                "Projects retrieved",
                Map.of("projects",projects)));
    }

    // UPDATE a project
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateProject(
            @PathVariable Integer id,
            @RequestBody ProjectDTO projectDTO
    ) {
        Project updated = projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(new ApiResponseDTO(
                true,
                "Project updated",
                Map.of("project", updated)));
    }

    // DELETE a project
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new ApiResponseDTO(true, "Project deleted", null));
    }
}
