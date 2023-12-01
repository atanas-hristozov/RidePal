package com.example.ridepal.controllers.mvc;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.exceptions.TextLengthException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.PlaylistDisplayFilterDto;
import com.example.ridepal.models.dtos.UserCreateUpdatePhoto;
import com.example.ridepal.models.dtos.UserFilterDto;
import com.example.ridepal.models.dtos.UserUpdateDto;
import com.example.ridepal.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserMvcController(UserService userService,
                             AuthenticationHelper authenticationHelper,
                             UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            Object currentUser = session.getAttribute("currentUser");
            User user = userService.getByUsername(currentUser.toString());
            return user.isAdmin();
        }
        return false;
    }

    @GetMapping()
    public String showUserPage(Model model, HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByUsername(username);
            model.addAttribute("user", user);
            return "My_Profile";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/update")
    public String showEditUserPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            UserUpdateDto userUpdateDto = userMapper.fromUserToUserUpdate(user);
            model.addAttribute("currentUser", userUpdateDto);

            return "Edit_User";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }


    @PostMapping("/update")
    public String updateUserProfile(@Valid @ModelAttribute("currentUser") UserUpdateDto userUpdateDto,
                                    BindingResult bindingResult,
                                    Model model,
                                    HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "Edit_User";
        }

        try {
            User userToUpdate = userMapper.fromUserUpdateDto(user.getId(), userUpdateDto);
            userService.update(userToUpdate);
            model.addAttribute("currentUser", userUpdateDto);

            return "redirect:/user";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";

        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "duplicate_email", e.getMessage());

            return "Edit_User";

        } catch (TextLengthException e) {
            bindingResult.rejectValue("firstName", "invalid_length", e.getMessage());

            return "Edit_User";
        }
    }

    @GetMapping("/photo")
    public String showEditUserProfilePhoto(Model model,
                                           HttpSession session,
                                           UserCreateUpdatePhoto userCreateUpdatePhoto) {
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            User user = userMapper.fromUserCreateUpdatePhotoDto(loggedUser.getId(), userCreateUpdatePhoto);
            model.addAttribute("currentUser", user);

            return "Upload_photo";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/photo")
    public String updateUserProfilePicture(@Valid @ModelAttribute("currentUser") UserCreateUpdatePhoto userCreateUpdatePhoto,
                                           BindingResult bindingResult,
                                           Model model,
                                           HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "Edit_User";
        }

        try {
            User userToUpdate = userMapper.fromUserCreateUpdatePhotoDto(user.getId(), userCreateUpdatePhoto);
            userService.update(userToUpdate);
            model.addAttribute("currentUser", userToUpdate);

            return "redirect:/user";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";

        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteUserProfile(HttpSession session, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/user";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            userService.delete(user);
            session.removeAttribute("user");
            session.removeAttribute("isAuthenticated");
            return "Index";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/admin")
    public String showAdminPage(@ModelAttribute("userFilterOptions") UserFilterDto userFilterDto,
                                Model model,
                                HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByUsername(username);
            if (user.isAdmin() || user.getId() == 1) {
                UserFilterOptions filterOptions = new UserFilterOptions(
                        userFilterDto.getUsername(),
                        userFilterDto.getEmail(),
                        userFilterDto.getFirstName());
                List<User> users = userService.getAllByFilterOptions(filterOptions);
                model.addAttribute("user", user);
                model.addAttribute("filterUsers", userFilterDto);
                model.addAttribute("users", users);

                return "My_Profile";
            }
            return "My_Profile";
        } else {
            return "redirect:/auth/login";
        }
    }

}
