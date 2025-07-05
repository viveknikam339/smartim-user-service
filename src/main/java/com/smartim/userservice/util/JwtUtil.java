package com.smartim.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * Provides methods to generate tokens, extract claims, and validate tokens.
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing the JWT.
     * Loaded from application yaml.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * JWT expiration time in milliseconds.
     * Loaded from application yaml.
     */
    @Value("${jwt.expiration}")
    private long jwtExpirationTimeMs;

    /**
     * Generates a JWT token containing username and role as claims.
     *
     * @param userName the username to include in the token
     * @param role the role of the user
     * @return a signed JWT token as a String
     */
    public String generateToken(String userName, String role){
        return Jwts.builder()
                .setSubject(userName)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTimeMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }

    /**
     * Extracts all claims from a given JWT token.
     *
     * @param token the JWT token
     * @return the claims (payload) in the token
     */
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username/email stored in the token
     */
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts the user's role from the JWT token.
     *
     * @param token the JWT token
     * @return the user's role
     */
    public String extractRole(String token){
        return (String) extractAllClaims(token).get("role");
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token has expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Validates the JWT token by checking the username and expiration.
     *
     * @param token the JWT token
     * @param userName the expected username
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token, String userName){
        final String username = extractUsername(token);
        return (username.equals(userName) && !isTokenExpired(token));
    }
}
