package com.example.ridepal.models.dtos;

public class UserAdminRightsDto {

    private boolean isAdmin;
    private byte[] userPhoto;

    public UserAdminRightsDto() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }
}
