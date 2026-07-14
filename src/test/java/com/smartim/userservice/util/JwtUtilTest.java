package com.smartim.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String secret = "a-very-long-and-secure-secret-key-for-testing-purposes-only";
    private final long expiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationTimeMs", expiration);
    }

    @Test
    void generateToken_andExtractClaims_shouldSucceed() {
        String token = jwtUtil.generateToken("testuser", "USER", "test@example.com");
        assertNotNull(token);

        Claims claims = jwtUtil.extractAllClaims(token);
        assertEquals("testuser", claims.getSubject());
        assertEquals("USER", claims.get("role"));
        assertEquals("test@example.com", claims.get("email"));
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken("testuser", "USER", "test@example.com");
        String username = jwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void extractRole_shouldReturnCorrectRole() {
        String token = jwtUtil.generateToken("testuser", "ADMIN", "test@example.com");
        String role = jwtUtil.extractRole(token);
        assertEquals("ADMIN", role);
    }

    @Test
    void isTokenValid_shouldReturnTrue_forValidToken() {
        String token = jwtUtil.generateToken("testuser", "USER", "test@example.com");
        assertTrue(jwtUtil.isTokenValid(token, "testuser"));
    }

    @Test
    void isTokenValid_shouldReturnFalse_forInvalidUsername() {
        String token = jwtUtil.generateToken("testuser", "USER", "test@example.com");
        assertFalse(jwtUtil.isTokenValid(token, "wronguser"));
    }

    @Test
    void isTokenExpired_shouldReturnTrue_forExpiredToken() {
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationTimeMs", -1000L); // Expired 1 second ago
        String expiredToken = jwtUtil.generateToken("testuser", "USER", "test@example.com");
        assertTrue(jwtUtil.isTokenExpired(expiredToken));
    }

    @Test
    void isTokenValid_shouldReturnFalse_forExpiredToken() {
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationTimeMs", -1000L);
        String expiredToken = jwtUtil.generateToken("testuser", "USER", "test@example.com");
        assertFalse(jwtUtil.isTokenValid(expiredToken, "testuser"));
    }

    @Test
    void extractAllClaims_shouldThrowException_forInvalidSignature() {
        String token = jwtUtil.generateToken("testuser", "USER", "test@example.com");

        // Create another util with a different secret
        JwtUtil anotherJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(anotherJwtUtil, "jwtSecret", "another-different-secret-key-for-sure");

        assertThrows(SignatureException.class, () -> anotherJwtUtil.extractAllClaims(token));
    }
}