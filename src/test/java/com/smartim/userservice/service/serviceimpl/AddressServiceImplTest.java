package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.dto.AddAddressRequest;
import com.smartim.userservice.dto.AddressDto;
import com.smartim.userservice.dto.UpdateAddressRequest;
import com.smartim.userservice.entity.Address;
import com.smartim.userservice.entity.User;
import com.smartim.userservice.exception.RequestProcessingException;
import com.smartim.userservice.exception.ResourceNotFoundException;
import com.smartim.userservice.mapper.AddressMapper;
import com.smartim.userservice.repository.AddressRepository;
import com.smartim.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;
    private AddressDto addressDto;
    private AddAddressRequest addAddressRequest;
    private UpdateAddressRequest updateAddressRequest;
    private User user;

    private static final String USER_NAME = "test@example.com";

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setId(1L);
        address.setUserName(USER_NAME);

        addressDto = new AddressDto();
        addressDto.setId(1L);

        addAddressRequest = new AddAddressRequest();
        addAddressRequest.setUserName(USER_NAME);

        updateAddressRequest = new UpdateAddressRequest();
        updateAddressRequest.setId(1L);

        user = new User();
        user.setUserName(USER_NAME);
    }

    @Test
    void getAddresses_Success() {
        when(addressRepository.findByUserName(USER_NAME)).thenReturn(List.of(address));
        when(addressMapper.toAddressDtoListFromAddressList(List.of(address))).thenReturn(List.of(addressDto));

        List<AddressDto> result = addressService.getAddresses(USER_NAME);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(addressDto.getId(), result.getFirst().getId());
        verify(addressRepository, times(1)).findByUserName(USER_NAME);
    }

    @Test
    void getAddresses_ThrowsResourceNotFoundException() {
        when(addressRepository.findByUserName(USER_NAME)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> addressService.getAddresses(USER_NAME));
        verify(addressRepository, times(1)).findByUserName(USER_NAME);
    }

    @Test
    void addAddress_Success() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        when(addressRepository.findByUserName(USER_NAME)).thenReturn(new ArrayList<>());
        when(addressMapper.toAddressEntity(addAddressRequest)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDtoFromAddress(address)).thenReturn(addressDto);

        AddressDto result = addressService.addAddress(USER_NAME, addAddressRequest);

        assertNotNull(result);
        assertEquals(addressDto.getId(), result.getId());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void addAddress_UserNotFound_ThrowsResourceNotFoundException() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.addAddress(USER_NAME, addAddressRequest));
        verify(userRepository, times(1)).findByUserName(USER_NAME);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void addAddress_MoreThan20Addresses_ThrowsRequestProcessingException() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        List<Address> addresses = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            addresses.add(new Address());
        }
        when(addressRepository.findByUserName(USER_NAME)).thenReturn(addresses);

        assertThrows(RequestProcessingException.class, () -> addressService.addAddress(USER_NAME, addAddressRequest));
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void updateAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDtoFromAddress(address)).thenReturn(addressDto);

        AddressDto result = addressService.updateAddress(USER_NAME, updateAddressRequest);

        assertNotNull(result);
        assertEquals(addressDto.getId(), result.getId());
        verify(addressMapper, times(1)).toAddressEntity(updateAddressRequest, address);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void updateAddress_AddressNotFound_ThrowsResourceNotFoundException() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(USER_NAME, updateAddressRequest));
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void deleteAddresses_Success() {
        List<Long> addressIds = List.of(1L, 2L);
        List<Address> addresses = List.of(address, new Address());
        when(addressRepository.findAllById(addressIds)).thenReturn(addresses);

        addressService.deleteAddresses(addressIds);

        verify(addressRepository, times(1)).findAllById(addressIds);
        verify(addressRepository, times(1)).deleteAll(addresses);
    }
}
