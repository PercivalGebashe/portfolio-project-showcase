package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.UserAccountRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final EmailService emailService;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository,
                              EmailService emailService) {
        this.userAccountRepository = userAccountRepository;
        this.emailService = emailService;
    }

    public void requestDeleteAccount(String emailAddress, String baseUrl) throws MessagingException {
        UserAccount userAccount = userAccountRepository.findByEmail(emailAddress)
                .orElseThrow(() -> new RuntimeException("User does not exist"));


        userAccount.setConfirmationToken(AuthService.generateVerificationToken());
        userAccount.setConfirmationTokenExpiration(LocalDateTime.now().plusHours(24));

        userAccountRepository.save(userAccount);

        sendConfirmationEmail(userAccount, baseUrl);
    }

    @Transactional
    public void confirmDelete(String verificationToken) {
        UserAccount userAccount = userAccountRepository
                .findByConfirmationToken(verificationToken)
                .orElseThrow(() -> new RuntimeException("Invalid confirmation token"));

        if (userAccount.getConfirmationTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Confirmation token expired");
        }
        userAccountRepository.delete(userAccount);
        System.out.println("Account deleted");
    }

    public UserAccount findAccountByEmail(String email){
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Async
    public void sendConfirmationEmail(UserAccount userAccount, String baseUrl) throws MessagingException {
        String token = userAccount.getConfirmationToken();
        String subject = "Confirm Account Deletion";

        String htmlMessage = String.format("""
        <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; margin:0; padding:20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #222; font-size: 22px; margin-bottom: 15px;">Delete Your Account</h2>
                    <p style="font-size: 16px; color: #333; line-height: 1.5;">
                        Click the button below to confirm your account deletion:
                    </p>
                    <!-- Confirmation Button -->
                    <div style="text-align: center; margin: 25px 0;">
                        <a href="%s/user/confirm-delete?token=%s"
                            style="display: inline-block; padding: 15px 30px; font-size: 16px; background-color: #666666; color: #ffffff; text-decoration: none; border-radius: 6px; font-weight: bold; width: 80%%; max-width: 300px;">
                        Delete Account
                        </a>
                    </div>
                    <!-- Copy link fallback -->
                    <p style="font-size: 14px; color: #555; line-height: 1.4;">
                        If the button doesn't work, copy and paste the following link into your browser:
                    </p>
                    <p style="word-break: break-all; font-size: 14px; color: #0073aa; line-height: 1.4;">
                        %s/user/confirm-delete?token=%s
                    </p>
                    <p style="font-size: 12px; color: #888; margin-top: 25px;">
                        If you did not request this, please ignore this email.
                    </p>
                </div>
            </body>
        </html>
        """, baseUrl, token, baseUrl, token);

        emailService.sendVerification(userAccount.getEmail(), subject, htmlMessage);
    }

    public UserAccount findByUserId(Integer userId) {

        return userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public boolean existsById(Integer userId) {
        return userAccountRepository.existsById(userId);
    }
}
