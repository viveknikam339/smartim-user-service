package com.smartim.userservice.mapper;

import com.smartim.userservice.dto.AddAddressRequest;
import com.smartim.userservice.dto.AddressDto;
import com.smartim.userservice.dto.UpdateAddressRequest;
import com.smartim.userservice.entity.Address;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);

    @Test
    void toAddressEntity_fromAddAddressRequest() {
        AddAddressRequest addAddressRequest = getAddAddressRequest();

        Address address = addressMapper.toAddressEntity(addAddressRequest);

        assertNotNull(address);
        assertEquals("Test User", address.getUserName());
        assertEquals("Test Receiver", address.getReceiverName());
        assertEquals("9876543210", address.getMobileNumber());
        assertEquals("HOME", address.getLabel());
        assertEquals("Test City", address.getCity());
        assertEquals("Test Street", address.getLine1());
        assertEquals("Test State", address.getState());
        assertEquals("123456", address.getPostalCode());
        assertEquals("INDIA", address.getCountry());
    }

    @Test
    void toAddressEntity_fromUpdateAddressRequest() {
        UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
        updateAddressRequest.setCity("Updated City");

        Address address = new Address();
        address.setCity("Original City");

        addressMapper.toAddressEntity(updateAddressRequest, address);

        assertEquals("Updated City", address.getCity());
    }

    @Test
    void toAddressDtoFromAddress() {
        Address address = new Address();
        address.setId(1L);
        address.setCity("Test City");

        AddressDto addressDto = addressMapper.toAddressDtoFromAddress(address);

        assertNotNull(addressDto);
        assertEquals(1L, addressDto.getId());
        assertEquals("Test City", addressDto.getCity());
    }

    @Test
    void toAddressDtoListFromAddressList() {
        Address address = new Address();
        address.setId(1L);
        address.setCity("Test City");

        List<Address> addresses = Collections.singletonList(address);
        List<AddressDto> addressDtos = addressMapper.toAddressDtoListFromAddressList(addresses);

        assertNotNull(addressDtos);
        assertEquals(1, addressDtos.size());
        assertEquals(1L, addressDtos.get(0).getId());
    }

    private static @NonNull AddAddressRequest getAddAddressRequest() {
        AddAddressRequest addAddressRequest = new AddAddressRequest();
        addAddressRequest.setUserName("Test User");
        addAddressRequest.setReceiverName("Test Receiver");
        addAddressRequest.setMobileNumber("9876543210");
        addAddressRequest.setLabel("HOME");
        addAddressRequest.setCity("Test City");
        addAddressRequest.setLine1("Test Street");
        addAddressRequest.setState("Test State");
        addAddressRequest.setPostalCode("123456");
        addAddressRequest.setCountry("INDIA");
        return addAddressRequest;
    }
}