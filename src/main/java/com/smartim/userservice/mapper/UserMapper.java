package com.smartim.userservice.mapper;

import com.smartim.userservice.dto.RegisterRequest;
import com.smartim.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // Important for Spring injection
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUserEntity(RegisterRequest registerRequest);
}
