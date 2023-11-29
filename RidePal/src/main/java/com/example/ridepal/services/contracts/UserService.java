package com.example.ridepal.services.contracts;

import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.UserDisplayDto;

import java.util.List;

public interface UserService {

    public void create (User user);
    void delete (User user, User userToDelete);
    void update (User user, User userToUpdate);
    User getById (int id);
    User getByUsername(String username);
    List<User> getAllByFilterOptions(UserFilterOptions userFilterOptions);

}
