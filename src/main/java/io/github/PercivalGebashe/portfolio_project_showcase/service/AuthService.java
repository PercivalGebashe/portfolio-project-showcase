package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.RegistrationRequestDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.EmailValidationStatus;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Role;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.EmailValidationStatusRepository;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.RoleRepository;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.UserAccountRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService{

    private final UserAccountRepository userAccountRepository;
    private final EmailValidationStatusRepository emailValidationStatusRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserAccountRepository userAccountRepository, EmailValidationStatusRepository emailValidationStatusRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userAccountRepository = userAccountRepository;
        this.emailValidationStatusRepository = emailValidationStatusRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }



    public Integer registerUser(RegistrationRequestDTO requestDTO, String baseUrl) throws MessagingException {
        UserAccount userAccountDetails = new UserAccount();
        EmailValidationStatus emailValidationStatus = emailValidationStatusRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Email validation status not found"));

        Role role = roleRepository.findById(1)
                        .orElseThrow(() -> new RuntimeException("Role id not found"));

        userAccountDetails.setEmail(requestDTO.emailAddress());
        userAccountDetails.setPasswordHash(passwordEncoder.encode(requestDTO.password()));
        userAccountDetails.setRole(role);
        userAccountDetails.setEmailValidationStatus(emailValidationStatus);
        userAccountDetails.setConfirmationToken(generateVerificationToken());
        userAccountDetails.setConfirmationTokenExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(24)).toLocalDateTime());

        System.out.println("To save: " + userAccountDetails);

        UserAccount savedAccountDetails =  userAccountRepository.save(userAccountDetails);

        System.out.println("Saved: " + savedAccountDetails);
        sendVerificationEmail(savedAccountDetails, baseUrl);

        return savedAccountDetails.getUserId();
    }



    public void verifyUser(String verificationToken){
        Optional<UserAccount> userAccountOptional = userAccountRepository.findByConfirmationToken(verificationToken);

        if(userAccountOptional.isPresent()){
            UserAccount userAccount = userAccountOptional.get();

            if(userAccount.getConfirmationTokenExpiration().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification Token expired");
            }

            EmailValidationStatus verifiedStatus = emailValidationStatusRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Email validation status not found"));

            userAccount.setEmailValidationStatus(verifiedStatus);
            userAccount.setConfirmationToken(null);
            userAccount.setConfirmationTokenExpiration(null);
            userAccountRepository.save(userAccount);

        }else {
            throw new RuntimeException("Invalid verification token");
        }
    }

    public void sendVerificationToken(String emailAddress, String baseUrl) throws MessagingException {
        Optional<UserAccount> userLoginOptional = userAccountRepository.findByEmail(emailAddress);

        if(userLoginOptional.isPresent()){
            UserAccount userAccount = userLoginOptional.get();
            if(userAccount.isEnabled()){
                throw new RuntimeException("Account is already verified");
            }
            userAccount.setConfirmationToken(generateVerificationToken());
            userAccount.setConfirmationTokenExpiration(LocalDateTime.now().plusHours(24));
            sendVerificationEmail(userAccount, baseUrl);
            userAccountRepository.save(userAccount);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    @Async
    public void sendVerificationEmail(UserAccount userAccount, String baseUrl) throws MessagingException {

        String subject = "Account Verification";
        String htmlMessage = String.format("""
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    </head>
    <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; margin:0; padding:20px;">
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
            <h2 style="color: #222; font-size: 22px; margin-bottom: 15px;">Verify Your Account</h2>
            <p style="font-size: 16px; color: #333; line-height: 1.5;">
                Thank you for registering! Please click the button below to verify your email address and activate your account.
            </p>
            
            <!-- Verification Button -->
            <div style="text-align: center; margin: 25px 0;">
                <a href="%s/api/v1/auth/verify?token=%s"
                   style="display: inline-block; padding: 15px 30px; font-size: 16px; background-color: #666666; color: #ffffff; text-decoration: none; border-radius: 6px; font-weight: bold; width: 80%%; max-width: 300px;">
                   Verify Account
                </a>
            </div>
            
            <!-- Copy link fallback -->
            <p style="font-size: 14px; color: #555; line-height: 1.4;">
                If the button doesn't work, copy and paste the following link into your browser:
            </p>
            <p style="word-break: break-all; font-size: 14px; color: #0073aa; line-height: 1.4;">
                %s/api/v1/auth/verify?token=%s
            </p>
            
            <p style="font-size: 12px; color: #888; margin-top: 25px;">
                If you did not request this, please ignore this email.
            </p>
        </div>
    </body>
</html>
""", baseUrl, userAccount.getConfirmationToken(), baseUrl, userAccount.getConfirmationToken());


        emailService.sendVerification(userAccount.getUsername(), subject, htmlMessage);
    }

    public static String generateVerificationToken() {
        byte[] randomBytes = new byte[24];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
        return base64Encoder.encodeToString(randomBytes);
    }
}
