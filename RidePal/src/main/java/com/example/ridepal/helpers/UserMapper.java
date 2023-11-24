package com.example.ridepal.helpers;

import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.UserAdminRightsDto;
import com.example.ridepal.models.dtos.UserCreateDto;
import com.example.ridepal.models.dtos.UserUpdateDto;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService userService;
@Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromUserCreateDto(UserCreateDto userCreateDto){
    User user = new User();
    user.setFirstName(userCreateDto.getFirstName());
    user.setLastName(userCreateDto.getLastName());
    user.setUsername(userCreateDto.getUsername());
    user.setPassword(userCreateDto.getPassword());
    user.setEmail(userCreateDto.getEmail());
    user.setUserPhoto(userCreateDto.getUserPhoto());

    return user;
    }

    public User fromUserUpdateDto(int id, UserUpdateDto userUpdateDto){
        User user = userService.getById(id);
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setPassword(userUpdateDto.getPassword());
        user.setEmail(userUpdateDto.getEmail());
        user.setUserPhoto(userUpdateDto.getUserPhoto());

        return user;
    }

    public User fromUserAdminRightsDto(int id, UserAdminRightsDto adminRightsDto) {
    User user = userService.getById(id);
    user.setAdmin(adminRightsDto.isAdmin());

    return user;
    }
}
