package com.example.ridepal.services;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.User;
import com.example.ridepal.repositories.AbstractUpdateDeleteRepository;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    public static final String ERROR_MESSAGE = "You are not authorized!";

    private final AbstractUpdateDeleteRepository<User> userAbstractIdentificationUdCrRepository;

    @Autowired
    public UserServiceImpl(AbstractUpdateDeleteRepository<User> userAbstractIdentificationUdCrRepository) {
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

    private void checkUsernameExist(User user) {
        boolean usernameExists = true;
        try {
            userAbstractIdentificationUdCrRepository.getByField(user.getUsername(), user);
        } catch (EntityNotFoundException e) {
            usernameExists = false;
        }
        if (usernameExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }
    private void checkEmailExist(User user) {
        boolean emailExists = true;
        try {
            userAbstractIdentificationUdCrRepository.getByField(user.getEmail(), user);
        } catch (EntityNotFoundException e) {
            emailExists = false;
        }
        if (emailExists) {
            throw new EntityDuplicateException("User", "email", user.getUsername());
        }
    }

    private static void checkAccessPermissions(User user, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != user.getId()) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
