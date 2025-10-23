package io.github.PercivalGebashe.portfolio_project_showcase;

import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    /**
     * Returns the authenticated UserAccount from the security context,
     * or null if no valid user is authenticated.
     */
    public UserAccount getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserAccount user) {
            return user;
        }

        return null;
    }

    /**
     * Verifies that the authenticated user matches the given userId.
     */
    public boolean isUserAuthorized(Integer userId) {
        UserAccount authenticatedUser = getAuthenticatedUser();
        return authenticatedUser != null && authenticatedUser.getUserId().equals(userId);
    }
}