package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProjectDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Project;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.service.MediaService;
import io.github.PercivalGebashe.portfolio_project_showcase.service.ProfileService;
import io.github.PercivalGebashe.portfolio_project_showcase.service.ProjectService;
import io.github.PercivalGebashe.portfolio_project_showcase.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProfileService profileService;
    private final UserAccountService userAccountService;
    private final ProjectService projectService;
    private final MediaService mediaService;

    @Autowired
    public ProjectController(ProfileService profileService, UserAccountService userAccountService, ProjectService projectService, MediaService mediaService) {
        this.profileService = profileService;
        this.userAccountService = userAccountService;
        this.projectService = projectService;
        this.mediaService = mediaService;
    }

    @GetMapping("/create/{userId}")
    public String showCreateProjectForm(
            @PathVariable Integer userId,
            Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){

            Object principal = authentication.getPrincipal();

            if(principal instanceof UserAccount authenticatedUser) {

                UserAccount dbUser = profileService.findByUserId(userId).getUserAccount();

                if(dbUser != null && dbUser.getUserId().equals(authenticatedUser.getUserId())) {

                    model.addAttribute("loggedInUser", dbUser);
                    model.addAttribute("projectDTO", new ProjectDTO());
                    return "project-upload";
                }
            }
        }
        return "redirect:/profile/" + userId;
    }

    @PostMapping("/create/{userId}")
    public String createProject(
            @PathVariable Integer userId,
            @ModelAttribute ProjectDTO projectDTO,
            @RequestParam("image") List<MultipartFile> files
    ) throws ExecutionException, InterruptedException {
        files.forEach(file -> System.out.println("file name: " + file.getOriginalFilename()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            Object principal = authentication.getPrincipal();

            if(principal instanceof UserAccount authenticatedUser){
                UserAccount dbUser = profileService.findByUserId(userId).getUserAccount();
                if (dbUser != null && dbUser.getUserId().equals(authenticatedUser.getUserId())) {
                    Project savedProject = projectService.createProject(userId, projectDTO);
                    List<String> imageUrls = mediaService.upLoadImages(savedProject.getProjectId(), files).get();
                    savedProject.setImageUrls(imageUrls);
                    projectService.updateProject(savedProject);
                }
            }
        }
        return "redirect:/profile/" + userId;
    }
}
