package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.entity.User;
import com.smartim.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private static final String USER_NAME = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String ROLE = "ADMIN";

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername(){
        User user = new User();
        user.setUserName(USER_NAME);
        user.setPassword(PASSWORD);
        user.setRole(ROLE);
        Mockito.when(repo.findByUserName(USER_NAME)).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(USER_NAME);

        Assertions.assertEquals(user.getUserName(), result.getUsername());
        Assertions.assertEquals(user.getPassword(), result.getPassword());
        Assertions.assertEquals(user.getRole(), result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).findFirst().orElse(null));
    }


}
