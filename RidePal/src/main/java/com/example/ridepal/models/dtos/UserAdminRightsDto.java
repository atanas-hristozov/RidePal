package com.example.ridepal.models.dtos;

public class UserAdminRightsDto {

    private boolean isAdmin;

    public UserAdminRightsDto() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
