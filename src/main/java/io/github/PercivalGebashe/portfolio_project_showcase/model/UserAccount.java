package io.github.PercivalGebashe.portfolio_project_showcase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_accounts", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAccount implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "email_validation_status_id", nullable = false)
    private EmailValidationStatus emailValidationStatus;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "confirmation_token_expiration")
    private LocalDateTime confirmationTokenExpiration;

    @Column(name = "recovery_token")
    private String recoveryToken;

    @Column(name = "recovery_token_expiration")
    private LocalDateTime recoveryTokenExpiration;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getRoleEnum().name()));
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return null != this.emailValidationStatus &&
                null != this.emailValidationStatus.getEmailValidationStatusId() &&
                Integer.valueOf(2).equals(emailValidationStatus.getEmailValidationStatusId());
    }
}
