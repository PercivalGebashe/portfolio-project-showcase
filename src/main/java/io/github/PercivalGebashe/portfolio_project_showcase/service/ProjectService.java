package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProjectDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Project;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.ProjectRepository;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserAccountRepository userAccountRepository;

    public ProjectService(ProjectRepository projectRepository, UserAccountRepository userAccountRepository) {
        this.projectRepository = projectRepository;
        this.userAccountRepository = userAccountRepository;
    }


    @Transactional
    public Project createProject(Integer userId, ProjectDTO projectDTO) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setUserAccount(user);
        project.setTitle(projectDTO.getTitle());
        project.setSummary(projectDTO.getSummary());
        project.setRepoLink(projectDTO.getRepoLink());
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }

    // Get by ID
    public Project getProjectById(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    // Get all projects for a user
    public List<Project> getProjectsForUser(Integer userId) {
        boolean exists = userAccountRepository.existsById(userId);
                if(!exists){
                    throw new RuntimeException("User not found");
                }
        return projectRepository.findByUserAccount_UserId(userId);
    }

    // Update project
    @Transactional
    public Project updateProject(Integer projectId, ProjectDTO projectDTO) {
        Project project = getProjectById(projectId);

        project.setTitle(projectDTO.getTitle());
        project.setSummary(projectDTO.getSummary());
        project.setContent(projectDTO.getContent());
        project.setRepoLink(projectDTO.getRepoLink());
        project.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Project project){
        return projectRepository.save(project);
    }

    // Delete project
    @Transactional
    public void deleteProject(Integer projectId) {
        Project project = getProjectById(projectId);
        projectRepository.delete(project);
    }

    public Project findByProjectId(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Project with ID: %s. Not Found,", projectId)));
    }
}
