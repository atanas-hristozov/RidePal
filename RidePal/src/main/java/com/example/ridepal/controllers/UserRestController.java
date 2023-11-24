package com.example.ridepal.controllers;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.UserAdminRightsDto;
import com.example.ridepal.models.dtos.UserCreateDto;
import com.example.ridepal.models.dtos.UserUpdateDto;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.example.ridepal.services.UserServiceImpl.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/roadbeat")
public class UserRestController {

    private static final String CAN_T_BE_EMPTY = "Cannot be empty!";
    private static final String CAN_T_DELETE_OTHER_ACCOUNT = "You can't delete other people's accounts!";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public User get(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return userService.getById(id);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
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

    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers,
                       @RequestBody UserUpdateDto userUpdateDto,
                       @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            checkIsItSameUser(loggedUser, id);
            User user = userMapper.fromUserUpdateDto(id, userUpdateDto);

            userService.update(user);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            int identicalNum = loggedUser.getId();
            User userToDelete = userService.getById(id);
            if (identicalNum == id || identicalNum == 1) {
                userService.delete(id);
            } else {
                throw new AuthorizationException(ERROR_MESSAGE);
            }

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @PutMapping("/admin/{id}")
    public void adminRightsUpdate(@RequestHeader HttpHeaders headers,
                                  @RequestBody UserAdminRightsDto adminRightsDto,
                                  @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);

            checkAdminRights(loggedUser);

            User userToUpdate = userService.getById(id);
            if (userToUpdate.getId() == 1 && loggedUser.getId() != 1) {
                throw new AuthorizationException(ERROR_MESSAGE);
            }

            userToUpdate = userMapper.fromUserAdminRightsDto(id, adminRightsDto);
            userService.update(userToUpdate);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static void checkAdminRights(User userToCheck) { // User with id 1 is always with admin rights
        if (!userToCheck.isAdmin() || userToCheck.getId() != 1) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private static void checkIsItSameUser(User loggedUser, int id) {
        if (loggedUser.getId() != id) {
            throw new AuthorizationException(CAN_T_DELETE_OTHER_ACCOUNT);
        }
    }
}
