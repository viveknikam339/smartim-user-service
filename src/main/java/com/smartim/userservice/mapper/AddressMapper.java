package com.smartim.userservice.mapper;

import com.smartim.userservice.dto.*;
import com.smartim.userservice.entity.Address;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Mapper interface for converting between Address entity and DTOs and vice versa.
 * Implemented automatically by MapStruct during build time.
 */
@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface AddressMapper {

    /**
     * Static instance for manual use outside Spring (optional).
     */
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);


    /**
     * Converts an add address request DTO to an Address entity.
     *
     * @param addAddressRequest the request containing address related information to add address
     * @return the corresponding Address entity
     */
    @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "userName")
    Address toAddressEntity(AddAddressRequest addAddressRequest);

    /**
     * Converts an update request DTO to an Address entity.
     *
     * @param updateAddressRequest the request containing Address input during update
     * @return the corresponding Address entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toAddressEntity(UpdateAddressRequest updateAddressRequest, @MappingTarget Address address,
                      @Context String updatedBy, @Context LocalDateTime updatedOn);

    /**
     * Converts an Address entity to a AddressDto.
     *
     * @param address the Address entity
     * @return the corresponding AddressDto
     */
    AddressDto toAddressDtoFromAddress(Address address);

    /**
     * Converts a list of Address entity to a list of AddressDto.
     *
     * @param addresses list of Address entity
     * @return list of corresponding AddressDto
     */
    List<AddressDto> toAddressDtoListFromAddressList(List<Address> addresses);
}
