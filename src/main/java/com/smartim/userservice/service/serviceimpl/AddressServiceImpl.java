package com.smartim.userservice.service.serviceimpl;

import com.smartim.userservice.dto.AddAddressRequest;
import com.smartim.userservice.dto.AddressDto;
import com.smartim.userservice.dto.UpdateAddressRequest;
import com.smartim.userservice.entity.Address;
import com.smartim.userservice.exception.RequestProcessingException;
import com.smartim.userservice.exception.ResourceNotFoundException;
import com.smartim.userservice.mapper.AddressMapper;
import com.smartim.userservice.repository.AddressRepository;
import com.smartim.userservice.repository.UserRepository;
import com.smartim.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link AddressService} to manage address operations for a user.
 * Supports retrieval, addition, update, and deletion of address records.
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    /**
     * Retrieves the list of addresses for the given user.
     *
     * @param userName the username (or email) of the user
     * @return List of {@link AddressDto} representing the user's addresses
     * @throws ResourceNotFoundException if the user has no addresses
     */
    @Override
    public List<AddressDto> getAddresses(String userName) {
        List<Address> addresses = addressRepository.findByUserName(userName);
        if(addresses==null || addresses.isEmpty())
            throw new ResourceNotFoundException("Addresses", "user-name", userName);
        return addressMapper.toAddressDtoListFromAddressList(addresses);
    }

    /**
     * Adds a new address for the specified user.
     * Validates that the user exists and doesn't already have 20 addresses.
     *
     * @param userName the username of the user
     * @param address the address to add
     * @return the saved address as {@link AddressDto}
     * @throws ResourceNotFoundException if the user is not found
     * @throws RequestProcessingException if the user already has 20 addresses
     */
    @Override
    public AddressDto addAddress(String userName, AddAddressRequest address) {
        userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user-name", userName));
        List<Address> addressList = addressRepository.findByUserName(userName);
        if(addressList.size()==20)
            throw new RequestProcessingException("More than 20 address can't be added.");
        return addressMapper.toAddressDtoFromAddress(addressRepository.save(addressMapper.toAddressEntity(address)));
    }

    /**
     * Updates an existing address for the given user.
     *
     * @param userName the username of the user
     * @param updatedAddress the address data to update
     * @return the updated address as {@link AddressDto}
     * @throws ResourceNotFoundException if the address is not found by ID
     */
    @Override
    public AddressDto updateAddress(String userName, UpdateAddressRequest updatedAddress) {
        Address existing = addressRepository.findById(updatedAddress.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Address", "user-name", userName));
        addressMapper.toAddressEntity(updatedAddress, existing, userName, LocalDateTime.now());

        return addressMapper.toAddressDtoFromAddress(addressRepository.save(existing));
    }

    /**
     * Deletes all addresses with the given IDs.
     *
     * @param addressIds list of address IDs to delete
     */
    @Override
    public void deleteAddresses(List<Long> addressIds) {
        List<Address> address = addressRepository.findAllById(addressIds);
        addressRepository.deleteAll(address);
    }
}

