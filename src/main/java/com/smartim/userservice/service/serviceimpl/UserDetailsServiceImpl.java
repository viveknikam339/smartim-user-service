package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.contants.UserConstants;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation of {@link UserDetailsService} for Spring Security.
 * Loads user-specific data by username (which is assumed to be the `userName` field in the User entity).
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repo;

    /**
     * Loads the user by their username and maps it to Spring Security's {@link UserDetails} object.
     *
     * @param username the username to search for (mapped to userName field)
     * @return a UserDetails object containing username, password, and authorities
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repo.findByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND)
        );
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole())));
    }
}
