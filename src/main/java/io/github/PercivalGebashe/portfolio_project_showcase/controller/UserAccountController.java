package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.service.UserAccountService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/user")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @DeleteMapping("/request-delete")
    public String requestDeleteAccount(@RequestParam(name = "email") String email)
            throws MessagingException {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        userAccountService.requestDeleteAccount(email, baseUrl);

        return "redirect:/auth/login";
    }

    @GetMapping("/confirm-delete")
    public String confirmDelete(@RequestParam String token) {
        userAccountService.confirmDelete(token);

        return "redirect:/auth/signup";
    }
}
