package com.smartim.userservice.service;

import com.smartim.userservice.dto.AddAddressRequest;
import com.smartim.userservice.dto.AddressDto;
import com.smartim.userservice.dto.UpdateAddressRequest;
import com.smartim.userservice.entity.Address;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    List<AddressDto> getAddresses(String userName);
    AddressDto addAddress(String userName, AddAddressRequest address);
    AddressDto updateAddress(String userName, UpdateAddressRequest updatedAddress);
    void deleteAddresses(List<Long> addressIds);
}
