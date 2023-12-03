package com.example.ridepal.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserCreateUpdatePhoto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] userPhoto;

    public UserCreateUpdatePhoto() {
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }
}
