package com.company.userservice.mapper;

import com.company.userservice.entity.UserEntity;
import com.company.userservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User map(UserEntity entity);

    UserEntity map(User user);
}
