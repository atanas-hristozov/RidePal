package com.example.ridepal.services;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.UserCreateUpdatePhotoDto;
import com.example.ridepal.models.dtos.UserDisplayDto;
import com.example.ridepal.repositories.AbstractCrudRepository;
import com.example.ridepal.repositories.contracts.UserRepository;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public void uploadPhoto(User user, MultipartFile multipartFile) throws IOException {
        user.setUserPhoto(multipartFile.getBytes());
        userRepository.update(user);
    }

    @Override
    public void removePhoto(User user) {
        user.setUserPhoto(null);
      userRepository.update(user);

    }

    @Override
    public void addRemoveAdmin(User executingUser, User userToUpdate) {
        checkAdminRights(executingUser);
        userToUpdate.setAdmin(!userToUpdate.isAdmin());
        userRepository.update(userToUpdate);
    }

    @Override
    public Long allUsersCount() {
        return userRepository.allUsersCount();
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
    private static void checkAdminRights(User userToCheck) {
        if (!userToCheck.isAdmin() && userToCheck.getId() != 1) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
