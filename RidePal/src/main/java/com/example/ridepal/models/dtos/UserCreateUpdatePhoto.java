package com.example.ridepal.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserCreateUpdatePhoto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userPhoto;

    public UserCreateUpdatePhoto() {
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
