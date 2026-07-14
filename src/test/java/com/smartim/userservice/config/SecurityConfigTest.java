package com.smartim.userservice.config;

import com.smartim.userservice.controller.UserAddressController;
import com.smartim.userservice.controller.UserAdminController;
import com.smartim.userservice.controller.UserController;
import com.smartim.userservice.security.JwtAuthFilter;
import com.smartim.userservice.service.AddressService;
import com.smartim.userservice.service.UserService;
import com.smartim.userservice.service.shared.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WithMockUser(username = "testuser")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UserAddressController userAddressController;

    @MockitoBean
    private UserAdminController userAdminController;

    @MockitoBean
    private UserController userController;

    @MockitoBean
    private AddressService addressService;

    @MockitoBean
    private RedisService redisService;

    @MockitoBean
    private UserService userService;

    @Test
    void whenUnauthenticated_thenRegisterEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/api/users/register"))
                .andExpect(status().isOk());
    }

    @Test
    void whenUnauthenticated_thenLoginEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/api/users/login"))
                .andExpect(status().isOk());
    }

    @Test
    void whenUnauthenticated_thenResetPasswordEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/api/users/resetPassword"))
                .andExpect(status().isOk());
    }

    @Test
    void whenUnauthenticated_thenSwaggerUiIsAccessible() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void whenUnauthenticated_thenApiDocsAreAccessible() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenUnauthenticated_thenProtectedEndpointIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAuthenticated_thenProtectedEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk());
    }
}