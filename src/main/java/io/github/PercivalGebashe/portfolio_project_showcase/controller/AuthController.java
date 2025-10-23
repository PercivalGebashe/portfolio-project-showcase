package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.LoginRequestDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.RegistrationRequestDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.service.AuthService;
import io.github.PercivalGebashe.portfolio_project_showcase.service.UserAccountService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserAccountService userAccountService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, UserAccountService userAccountService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userAccountService = userAccountService;
        this.authenticationManager = authenticationManager;
    }

    // ---------------------- SIGNUP PAGE ----------------------
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("registerRequest", new RegistrationRequestDTO());
        return "signup";
    }

    // ---------------------- PROCESS SIGNUP ----------------------
    @PostMapping("/signup")
    public String processSignup(
            @Valid @ModelAttribute("registerRequest") RegistrationRequestDTO registerRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            authService.registerUser(registerRequest, baseUrl);

            return "redirect:/auth/login";

        } catch (MessagingException e) {
            model.addAttribute("errorMessage", "Failed to send verification email. Try again later.");
            return "signup";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "signup";
        }
    }

    // ---------------------- LOGIN PAGE ----------------------
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "login";
    }

    // ---------------------- PROCESS LOGIN ----------------------
    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            UserAccount userAccount = authService
                    .authenticate(loginRequest.getEmailAddress().toLowerCase(), loginRequest.getPassword(), request);
            return "redirect:/profile/" + userAccount.getProfile().getUserId() ;

        } catch (BadCredentialsException ex) {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Login failed: " + ex.getMessage());
            return "login";
        }
    }

    // ---------------------- VERIFY ACCOUNT ----------------------
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String verificationToken,
                                RedirectAttributes redirectAttributes) {
        try {
            authService.verifyUser(verificationToken);
            redirectAttributes.addFlashAttribute("successMessage", "Account verified successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Verification failed: " + e.getMessage());
        }
        return "redirect:/auth/login";
    }

    // ---------------------- RESEND VERIFICATION ----------------------
    @GetMapping("/resend-verification")
    public String resendVerification(@RequestParam("email") String email,
                                     RedirectAttributes redirectAttributes) {
        try {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            authService.sendVerificationToken(email, baseUrl);
            redirectAttributes.addFlashAttribute("successMessage", "Verification email sent!");
        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send verification email.");
        } catch (RuntimeException ignored) {
            redirectAttributes.addFlashAttribute("successMessage", "Verification email sent!");
        }
        return "redirect:/auth/login";
    }
}