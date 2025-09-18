package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ApiResponseDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.LoginRequestDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.RefreshTokenRequestDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.RegistrationRequestDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.service.AuthService;
import io.github.PercivalGebashe.portfolio_project_showcase.service.JwtService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsService userDetailsService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // ---------------------- REGISTER ----------------------
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegistrationRequestDTO requestDTO) {
        try {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            Integer userId = authService.registerUser(requestDTO, baseUrl);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(
                            true,
                            "User registered successfully. Check email for account verification link.",
                            Map.of("userId", userId)
                    ));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Failed to send verification email", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, "Registration failed: " + e.getMessage(), null));
        }
    }

    // ---------------------- VERIFY ACCOUNT ----------------------
    @GetMapping("/verify")
    public ResponseEntity<ApiResponseDTO> verifyAccount(@RequestParam("token") String verificationToken) {
        try {
            authService.verifyUser(verificationToken);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Account verification successful", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, e.getMessage(), null));
        }
    }

    // ---------------------- RESEND VERIFICATION ----------------------
    @GetMapping("/resend-verification")
    public ResponseEntity<ApiResponseDTO> resendVerification(@RequestParam("email") String email) {
        try {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            authService.sendVerificationToken(email, baseUrl);

            // Always return success message to prevent email enumeration
            return ResponseEntity.ok(new ApiResponseDTO(true, "Email verification link sent", null));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Failed to send verification email", null));
        } catch (RuntimeException ignored) {
            return ResponseEntity.ok(new ApiResponseDTO(true, "Email verification link sent", null));
        }
    }

    // ---------------------- LOGIN ----------------------
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.emailAddress(), request.password())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            Map<String, Object> data = Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            );

            return ResponseEntity.ok(new ApiResponseDTO(true, "Login successful", data));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Invalid email or password", null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Login failed", null));
        }
    }

    // ---------------------- REFRESH TOKEN ----------------------
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO request) {
        try {
            String refreshToken = request.refreshToken();
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO(false, "Invalid or expired refresh token", null));
            }

            String newAccessToken = jwtService.generateToken(userDetails);
            Map<String, Object> data = Map.of("accessToken", newAccessToken);

            return ResponseEntity.ok(new ApiResponseDTO(true, "Token refreshed", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Invalid refresh token", null));
        }
    }
}