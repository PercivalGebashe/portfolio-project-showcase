package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProfileUpdateDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Profile;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Project;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.service.MediaService;
import io.github.PercivalGebashe.portfolio_project_showcase.service.ProfileService;
import io.github.PercivalGebashe.portfolio_project_showcase.service.ProjectService;
import jakarta.validation.Valid;
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
@RequestMapping("/profile")
public class ProfileController {


    private final ProfileService profileService;
    private final ProjectService projectService;
    private final MediaService mediaService;


    @Autowired
    public ProfileController(ProfileService profileService, ProjectService projectService, MediaService mediaService) {
        this.profileService = profileService;
        this.projectService = projectService;
        this.mediaService = mediaService;
    }

    // Display profile by user ID
    @GetMapping("/{userId}")
    public String getProfile(@PathVariable Integer userId, Model model) {
        Profile profile = profileService.findByUserId(userId);
        List<Project> projects = projectService.getProjectsForUser(userId);

        UserAccount loggedInUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            Object principal = authentication.getPrincipal();

            if(principal instanceof UserAccount authenticatedUser){
                if(profile != null && profile.getUserAccount().getUserId().equals(authenticatedUser.getUserId())){
                    loggedInUser = profile.getUserAccount();
                }
            }
        }

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("profile", profile);
        model.addAttribute("projects", projects);
        return "profile";
    }

    @PostMapping("/update/{userId}")
    public String updateProfile(
            @PathVariable Integer userId,
            @Valid @ModelAttribute("profileUpdate") ProfileUpdateDTO requestDto,
            @RequestParam("image") MultipartFile file) throws ExecutionException, InterruptedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){

            Object principal = authentication.getPrincipal();

            if(principal instanceof UserAccount authenticatedUser) {

                UserAccount dbUser = profileService.findByUserId(userId).getUserAccount();

                if(dbUser != null && dbUser.getUserId().equals(authenticatedUser.getUserId())) {
                    String imageUrl = mediaService.uploadProfileImage(userId, file).get();
                    requestDto.setProfilePictureUrl(imageUrl);
                    profileService.updateProfile(userId, requestDto);
                }
            }
        }

        return "redirect:/profile/" + userId;
    }

    @GetMapping("/update/{userId}")
    public String showUpdateForm(@PathVariable Integer userId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){

            Object principal = authentication.getPrincipal();

            if(principal instanceof UserAccount authenticatedUser){
                Profile profile = profileService.findByUserId(userId);

                if(profile != null && profile.getUserId().equals(authenticatedUser.getUserId())){

                    ProfileUpdateDTO dto = new ProfileUpdateDTO();
                    dto.setUserId(userId);
                    dto.setFirstName(profile.getFirstName());
                    dto.setLastName(profile.getLastName());
                    dto.setTagline(profile.getTagline());
                    dto.setBio(profile.getBio());
                    dto.setSkills(profile.getSkills());
                    dto.setContactLinks(profile.getContactLinks());

                    model.addAttribute("profileUpdate", dto);

                    return "update-profile";
                }
            }
        }
        return "redirect:/profile/" + userId;
    }
}
