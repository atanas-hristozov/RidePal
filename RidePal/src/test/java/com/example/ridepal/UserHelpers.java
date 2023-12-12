package com.example.ridepal;

import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.UserCreateDto;
import com.example.ridepal.models.dtos.UserUpdateDto;

public class UserHelpers {

    public static User createMockUser() {
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setPassword("MockPassword");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setUsername("MockUsername");
        mockUser.setEmail("Mock@mail");
        mockUser.setAdmin(false);
        return mockUser;
    }
    public static User createDifferentuser() {
        User mockUser = new User();
        mockUser.setId(3);
        mockUser.setPassword("NewPassword");
        mockUser.setFirstName("NewFirstName");
        mockUser.setLastName("NewLastName");
        mockUser.setUsername("NewUsername");
        mockUser.setEmail("NewMock@mail");
        mockUser.setAdmin(false);
        return mockUser;
    }
    public static User createAdmin(){
        User admin = new User();
        admin.setId(1);
        admin.setUsername("AdminUsername");
        admin.setPassword("123456");
        admin.setFirstName("AdminFirstName");
        admin.setLastName("AdminLastName");
        admin.setEmail("AdminEmail");
        admin.setAdmin(true);

        return admin;
    }
    public static UserCreateDto createUserCreateDto(){
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("UserCreateDto");
        userCreateDto.setEmail("UserCreateDto@mail");
        userCreateDto.setFirstName("UserCreateFirstNameDto");
        userCreateDto.setLastName("UserCreateLastNameDto");
        userCreateDto.setPassword("UserCreateDtoPassword");

        return userCreateDto;
    }
    public static UserUpdateDto createUserUpdateDto(){
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("UserUpdateFirstName");
        userUpdateDto.setLastName("UserUpdateLastName");
        userUpdateDto.setEmail("UserUpdate@mail");
        userUpdateDto.setPassword("UserUpdatePassword");

        return userUpdateDto;
    }
    public static UserFilterOptions createUserFilterOptions(){
        return new UserFilterOptions("username",
                "email",
                "firstName"
        );
    }
}
