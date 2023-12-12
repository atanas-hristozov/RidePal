package com.example.ridepal.controllers;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.*;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.example.ridepal.services.UserServiceImpl.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/roadbeat")
public class UserRestController {

    private static final String CAN_T_BE_EMPTY = "Cannot be empty!";

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("user/{id}")
    public UserDisplayDto get(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return userMapper.fromUser(userService.getById(id));

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/user")
    public User create(@RequestBody UserCreateDto userCreateDto) {
        User user = userMapper.fromUserCreateDto(userCreateDto);
        try {
            userService.create(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CAN_T_BE_EMPTY);
        }
        return user;
    }

    @PutMapping("/user/{id}")
    public void update(@RequestHeader HttpHeaders headers,
                       @RequestBody UserUpdateDto userUpdateDto,
                       @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            User userToUpdate = userMapper.fromUserUpdateDto(id, userUpdateDto);

            userService.update(userToUpdate);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/user/photo")
    public void addUpdatePhoto(@RequestHeader HttpHeaders headers,
                               @RequestBody UserCreateUpdatePhotoDto userCreateUpdatePhotoDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToUpdate = userMapper.fromUserCreateUpdatePhotoDto(user.getId(), userCreateUpdatePhotoDto);

            userService.update(userToUpdate);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToDelete = userService.getById(id);
            userService.delete(userToDelete);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/user/users")
    public List<UserDisplayDto> getAll(@RequestHeader HttpHeaders headers,
                                       UserFilterOptions userFilterOptions) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            checkAdminRights(loggedUser);
        List<UserDisplayDto> userDisplayDtos = new ArrayList<>();
        for (User user: userService.getAllByFilterOptions(userFilterOptions))
            userDisplayDtos.add(userMapper.fromUser(user));
        return userDisplayDtos;

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static void checkAdminRights(User userToCheck) {
        if (!userToCheck.isAdmin() && userToCheck.getId() != 1) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
