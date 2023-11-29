package com.example.ridepal.models;

import java.util.Optional;

public class UserFilterOptions {
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> firstName;

    public UserFilterOptions(String username,
                             String email,
                             String firstName) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }
}
