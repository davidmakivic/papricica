package com.restaurant.papricica.mapper;

import com.restaurant.papricica.dtos.UserDetailDto;
import com.restaurant.papricica.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailDto userToUserDetailDto(User user);

    List<UserDetailDto> usersToUserDetailDtos(List<User> users);
}
