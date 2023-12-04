package com.example.ridepal.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserCreateUpdatePhotoDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] userPhoto;

    public UserCreateUpdatePhotoDto() {
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }
}
