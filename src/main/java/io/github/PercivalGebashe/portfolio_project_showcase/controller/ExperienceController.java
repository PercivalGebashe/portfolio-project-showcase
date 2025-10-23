package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ExperienceUpdateDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.service.ExperienceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    @Autowired
    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping("/update/{userId}")
    public String populateExperienceUpdateForm(@PathVariable Integer userId,
                                               Model model,
                                               HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserAccount authenticatedUser) {

                UserAccount dbUser = experienceService.findUserById(userId);

                if (dbUser != null && dbUser.getUserId().equals(authenticatedUser.getUserId())) {
                    model.addAttribute("loggedInUser", dbUser);

                    return "experience-update";
                } else {
                    return "redirect:/profile/" + userId;
                }
            }
        }

        return "redirect:/profile/" + userId;
    }

    @PostMapping("/update/{userId}")
    public String updateExperience(@PathVariable Integer userId,
                                   @ModelAttribute("experiences") ExperienceUpdateDTO experiences) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            Object principal = authentication.getPrincipal();

            if(principal instanceof UserAccount authenticatedUser){
                UserAccount dbUser = experienceService.findUserById(userId);

                if(dbUser != null && dbUser.getUserId().equals(authenticatedUser.getUserId())){
                    experienceService.updateExperience(userId, experiences);
                }
            }
        }
        return "redirect:/profile/" + userId;
    }
}
