package com.smartim.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartim.userservice.dto.AddAddressRequest;
import com.smartim.userservice.dto.AddressDto;
import com.smartim.userservice.dto.UpdateAddressRequest;
import com.smartim.userservice.service.AddressService;
import com.smartim.userservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAddressController.class)
@WithMockUser(username = "testuser")
class UserAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddressDto addressDto;


    @BeforeEach
    void setUp() {
        addressDto = new AddressDto();
        addressDto.setId(1L);
        addressDto.setCity("Test City");
        addressDto.setReceiverName("testuser");
    }

    @Test
    void getAddresses_ShouldReturnListOfAddresses() throws Exception {
        when(addressService.getAddresses("testuser")).thenReturn(Collections.singletonList(addressDto));

        mockMvc.perform(get("/api/users/me/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].receiverName").value("testuser"));
    }

    @Test
    void addAddress_ShouldReturnSavedAddress() throws Exception {
        AddAddressRequest addAddressRequest = new AddAddressRequest();
        addAddressRequest.setCity("New City");
        addAddressRequest.setUserName("testuser");
        addAddressRequest.setReceiverName("testuser");

        when(addressService.addAddress(eq("testuser"), any(AddAddressRequest.class))).thenReturn(addressDto);

        mockMvc.perform(post("/api/users/me/addresses/addAddress")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addAddressRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.receiverName").value("testuser"));
    }

    @Test
    void updateAddress_ShouldReturnUpdatedAddress() throws Exception {
        UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
        updateAddressRequest.setId(1L);
        updateAddressRequest.setCity("Updated City");

        when(addressService.updateAddress(eq("testuser"), any(UpdateAddressRequest.class))).thenReturn(addressDto);

        mockMvc.perform(patch("/api/users/me/addresses/updateAddress")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAddressRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteAddress_ShouldReturnOk() throws Exception {
        List<Long> addressIds = Collections.singletonList(1L);
        doNothing().when(addressService).deleteAddresses(addressIds);

        mockMvc.perform(delete("/api/users/me/addresses/deleteAddresses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressIds)))
                .andExpect(status().isOk());
    }
}