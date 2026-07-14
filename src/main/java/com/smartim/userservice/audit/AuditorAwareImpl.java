package com.smartim.userservice.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.smartim.userservice.contants.UserConstants.DEFAULT_USER;

public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Retrieves the username of the currently authenticated user.
     *
     * @return username (typically email or username)
     */
    private Optional<String> getCurrentUserName() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken){
            return Optional.of(DEFAULT_USER);
        }
        return Optional.of(authentication.getName());
    }

    /**
     * Provides current auditor name.
     *
     * @return username (typically email or username)
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return  getCurrentUserName();
    }
}
