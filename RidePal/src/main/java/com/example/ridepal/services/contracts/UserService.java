package com.example.ridepal.services.contracts;

import com.example.ridepal.models.User;

import java.util.List;

public interface UserService {

    public void create (User user);
    void delete (User user, User userToDelete);
    void update (User user, User userToUpdate);
    User getById (int id);
    User getByUsername(String username);
    List<User> getAll();

}
