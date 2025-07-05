package com.smartim.userservice.mapper;

import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.dto.UserDto;
import com.smartim.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting between User entity and DTOs.
 * Implemented automatically by MapStruct during build time.
 */
@Mapper(componentModel = "spring") // Important for Spring injection
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
    User toUserEntity(RegisterRequest registerRequest);

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
     * @param user list of User entities
     * @return list of corresponding UserDto
     */
    List<UserDto> toUserDtoListFromUserList(List<User> user);
}
