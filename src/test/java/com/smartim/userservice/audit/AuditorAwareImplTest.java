package com.smartim.userservice.audit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.smartim.userservice.contants.UserConstants.DEFAULT_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditorAwareImplTest {

    private AuditorAwareImpl auditorAware;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AnonymousAuthenticationToken anonymousAuthenticationToken;

    @BeforeEach
    void setUp() {
        auditorAware = new AuditorAwareImpl();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentAuditor_AuthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");

        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        assertEquals(Optional.of("testuser"), currentAuditor);
    }

    @Test
    void testGetCurrentAuditor_UnauthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        assertEquals(Optional.of(DEFAULT_USER), currentAuditor);
    }

    @Test
    void testGetCurrentAuditor_AnonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);

        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        assertEquals(Optional.of(DEFAULT_USER), currentAuditor);
    }

    @Test
    void testGetCurrentAuditor_NoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        assertEquals(Optional.of(DEFAULT_USER), currentAuditor);
    }
}
