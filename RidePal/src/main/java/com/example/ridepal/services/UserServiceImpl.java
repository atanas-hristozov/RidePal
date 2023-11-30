package com.example.ridepal.services;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.UserDisplayDto;
import com.example.ridepal.repositories.AbstractCrudRepository;
import com.example.ridepal.repositories.contracts.UserRepository;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final String ERROR_MESSAGE = "You are not authorized!";

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        checkUsernameExist(user);
        user.setAdmin(false);
        userRepository.create(user);
    }

    @Override
    public void delete(User user) {

        userRepository.delete(user.getId());
    }

    @Override
    public void update(User userToUpdate) {
        userRepository.update(userToUpdate);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByField("username", username);
    }

    @Override
    public List<User> getAllByFilterOptions(UserFilterOptions userFilterOptions) {
        return userRepository.getAllByFilterOptions(userFilterOptions);

    }

    private void checkUsernameExist(User user) {
        try {
            userRepository.getByField("username", user.getUsername());
            // If the above line doesn't throw an exception, then the username already exists
            throw new EntityDuplicateException("User", "username", user.getUsername());
        } catch (EntityNotFoundException e) {
            // Username doesn't exist, which is what we want
        }
    }
    private static void checkAccessPermissions(User user, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != user.getId() && executingUser.getId() != 1) {
            // User with Id 1 have admin rights by default
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
