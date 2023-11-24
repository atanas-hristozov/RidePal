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

    private final AbstractCrudRepository<User> userAbstractIdentificationUdCrRepository;

    @Autowired
    public UserServiceImpl(AbstractCrudRepository<User> userAbstractIdentificationUdCrRepository) {
        this.userAbstractIdentificationUdCrRepository = userAbstractIdentificationUdCrRepository;
    }

    @Override
    public void create(User user) {
        checkEmailExist(user);
        checkUsernameExist(user);
        user.setAdmin(false);
        userAbstractIdentificationUdCrRepository.create(user);
    }

    @Override
    public void delete(int id) {
        userAbstractIdentificationUdCrRepository.delete(id);
    }

    @Override
    public void update(User user) {
        checkEmailExist(user);
        checkUsernameExist(user);
        userAbstractIdentificationUdCrRepository.update(user);
    }

    @Override
    public User getById(int id) {
        return userAbstractIdentificationUdCrRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userAbstractIdentificationUdCrRepository.getByField("username", username);
    }

    @Override
    public List<User> getAll() {
        return userAbstractIdentificationUdCrRepository.getAll();
    }

    private void checkUsernameExist(User user) {
        try {
            userAbstractIdentificationUdCrRepository.getByField("username", user.getUsername());
            // If the above line doesn't throw an exception, then the username already exists
            throw new EntityDuplicateException("User", "username", user.getUsername());
        } catch (EntityNotFoundException e) {
            // Username doesn't exist, which is what we want
        }
    }

    private void checkEmailExist(User user) {
        try {
            userAbstractIdentificationUdCrRepository.getByField("email", user.getEmail());
            throw new EntityDuplicateException("User", "email", user.getEmail());
        } catch (EntityNotFoundException e) {

        }
    }

    private static void checkAccessPermissions(User user, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != user.getId()) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
