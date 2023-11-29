package com.example.ridepal.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCreateDto {
    @NotNull(message = "Name can't be empty")
    @Size(min = 3, max = 32, message = "First name must be between 3 and 32 symbols.")
    private String firstName;
    @NotNull(message = "Name can't be empty")
    @Size(min = 3, max = 32, message = "First name must be between 3 and 32 symbols.")
    private String lastName;
    @NotNull(message = "Username can't be empty")
    private String username;
    @NotNull(message = "Password can't be empty")
    private String password;
    @NotNull
    private String email;

    public UserCreateDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
