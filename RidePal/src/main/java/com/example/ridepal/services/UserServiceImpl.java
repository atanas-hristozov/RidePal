package com.example.ridepal.services;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.User;
import com.example.ridepal.repositories.AbstractCrudRepository;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final String ERROR_MESSAGE = "You are not authorized!";

    private final AbstractCrudRepository<User> userAbstractCrudRepository;

    @Autowired
    public UserServiceImpl(AbstractCrudRepository<User> userAbstractCrudRepository) {
        this.userAbstractCrudRepository = userAbstractCrudRepository;
    }

    @Override
    public void create(User user) {
        checkEmailExist(user);
        checkUsernameExist(user);
        user.setAdmin(false);
        userAbstractCrudRepository.create(user);
    }

    @Override
    public void delete(User user, User userToDelete) {
        checkAccessPermissions(user, userToDelete);
        userAbstractCrudRepository.delete(userToDelete.getId());
    }

    @Override
    public void update(User user, User userToUpdate) {
        checkAccessPermissions(user, userToUpdate);
        checkEmailExist(user);
        checkUsernameExist(user);
        userAbstractCrudRepository.update(user);
    }

    @Override
    public User getById(int id) {
        return userAbstractCrudRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userAbstractCrudRepository.getByField("username", username);
    }

    @Override
    public List<User> getAll() {
        return userAbstractCrudRepository.getAll();
    }

    private void checkUsernameExist(User user) {
        try {
            userAbstractCrudRepository.getByField("username", user.getUsername());
            // If the above line doesn't throw an exception, then the username already exists
            throw new EntityDuplicateException("User", "username", user.getUsername());
        } catch (EntityNotFoundException e) {
            // Username doesn't exist, which is what we want
        }
    }

    private void checkEmailExist(User user) {
        try {
            userAbstractCrudRepository.getByField("email", user.getEmail());
            throw new EntityDuplicateException("User", "email", user.getEmail());
        } catch (EntityNotFoundException e) {

        }
    }

    private static void checkAccessPermissions(User user, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != user.getId() && executingUser.getId() != 1) {
            // User with Id 1 have admin rights by default
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
