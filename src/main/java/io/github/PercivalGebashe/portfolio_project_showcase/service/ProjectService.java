package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ParagraphContent;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProjectDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Project;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserAccountService userAccountService;
    private final MediaService mediaService;

    public ProjectService(ProjectRepository projectRepository, UserAccountService userAccountService, MediaService mediaService) {
        this.projectRepository = projectRepository;
        this.userAccountService = userAccountService;
        this.mediaService = mediaService;
    }

    // Get all projects for a user
    public List<Project> getProjectsForUser(Integer userId) {
        boolean exists = userAccountService.existsById(userId);
                if(!exists){
                    throw new RuntimeException("User not found");
                }
        return projectRepository.findByUserAccount_UserId(userId);
    }

    @Transactional
    public void createProject(
            Integer userId,
            ProjectDTO projectDTO,
            MultipartFile file,
            List<MultipartFile> images) throws ExecutionException, InterruptedException {
        UserAccount userAccount = userAccountService.findByUserId(userId);

        // --- process and upload image ---
        String projectImageUrl = mediaService.uploadImage(file).get();

        // --- Step 2: Upload images to ImageKit (if any) ---
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String filename = image.getOriginalFilename(); // e.g., "1_screen.png"
                if (filename == null) continue;
                int paraIndex = Integer.parseInt(filename.split("_")[0].substring(1))-1;


                // Call your service to upload the processed image
                String imageUrl = mediaService.uploadImage(image).get();

                // attach the image URL to the corresponding paragraph
                ParagraphContent paragraph = projectDTO.getContent().get(paraIndex);
                if (paragraph != null) {
                    paragraph.setImageUrl(imageUrl);
                }
            }
        }

        // --- Step 3: Prepare the Project entity ---
        Project project = new Project();
        project.setUserAccount(userAccount);
        project.setTitle(projectDTO.getTitle());
        project.setSummary(projectDTO.getSummary());
        project.setProjectImageUrl(projectImageUrl);
        project.setTechnologies(projectDTO.getTechnologies());
        project.setRepoLink(projectDTO.getRepoLink());
        project.setContent(projectDTO.getContent());

        // --- Step 4: Save the project ---
        projectRepository.save(project);

    }

    // Delete project
    @Transactional
    public void deleteProject(Integer projectId) {
        Project project = findByProjectId(projectId);
        projectRepository.delete(project);
    }

    public Project findByProjectId(Integer projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Project with ID: %s. Not Found,", projectId)));
    }
}
