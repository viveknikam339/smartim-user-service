package com.smartim.userservice.mapper;

import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.dto.UpdateUserRequest;
import com.smartim.userservice.dto.UserDto;
import com.smartim.userservice.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Mapper interface for converting between User entity and DTOs and vice versa.
 * Implemented automatically by MapStruct during build time.
 */
@Mapper(componentModel = "spring", imports = {LocalDateTime.class}) // Important for Spring injection
public interface UserMapper {

    /**
     * Static instance for manual use outside Spring (optional).
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Converts a registration request DTO to a User entity.
     *
     * @param registerRequest the request containing user input during registration
     * @return the corresponding User entity
     */
    @Mapping(target = "password", expression = "java(encoder.encode(registerRequest.getPassword()))")
    @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())")
    @Mapping(target = "userStatus", constant = "true")
    @Mapping(target = "createdBy", source = "userName")
    User toUserEntity(RegisterRequest registerRequest, @Context PasswordEncoder encoder);

    /**
     * Converts an update request DTO to a User entity.
     *
     * @param updateUserRequest the request containing user input during update
     * @return the corresponding User entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUserEntity(UpdateUserRequest updateUserRequest, @MappingTarget User user,
                      @Context String updatedBy, @Context LocalDateTime updatedOn);

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity
     * @return the corresponding UserDto
     */
    UserDto toUserDtoFromUser(User user);

    /**
     * Converts a list of User entities to a list of UserDto.
     *
     * @param user list of User entity
     * @return list of corresponding UserDto
     */
    List<UserDto> toUserDtoListFromUserList(List<User> user);
}
