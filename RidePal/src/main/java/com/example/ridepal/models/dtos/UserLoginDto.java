package com.example.ridepal.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserLoginDto {
    @NotNull(message = "Username can't be empty")
    private String username;
    @NotNull(message = "Password can't be empty")
    private String password;

    public UserLoginDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
