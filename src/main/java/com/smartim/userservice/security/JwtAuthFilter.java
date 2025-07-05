package com.smartim.userservice.security;

import com.smartim.userservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that validates JWT from incoming requests and sets the security context
 * for authenticated users. It runs once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Extracts JWT from the Authorization header, validates it, and if valid,
     * sets the authentication in the Spring SecurityContext.
     *
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain the filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;
        final String username;

        // If Authorization header is missing or doesn't start with Bearer, skip this filter
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT from the header
        jwtToken = authHeader.substring(7);

        try{
            // Extract username from token
            username = jwtUtil.extractUsername(jwtToken);
        } catch (Exception e) {
            // If token is invalid or extraction fails, skip authentication
            filterChain.doFilter(request, response);
            return;
        }

        // Proceed only if user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails =  userDetailsService.loadUserByUsername(username);

            // Validate the token with the user details
            if (jwtUtil.isTokenValid(jwtToken, userDetails.getUsername())){
                // Create authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
