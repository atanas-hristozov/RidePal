package com.example.ridepal.controllers.mvc;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.PlaylistDisplayFilterDto;
import com.example.ridepal.models.dtos.UserAdminRightsDto;
import com.example.ridepal.models.dtos.UserCreateUpdatePhotoDto;
import com.example.ridepal.models.dtos.UserFilterDto;
import com.example.ridepal.services.contracts.PlaylistService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.ridepal.services.UserServiceImpl.ERROR_MESSAGE;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {

    private UserService userService;
    private PlaylistService playlistService;
    private UserMapper userMapper;
    private AuthenticationHelper authenticationHelper;

    @Autowired
    public AdminMvcController(UserService userService,
                              PlaylistService playlistService, UserMapper userMapper,
                              AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.playlistService = playlistService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
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

    @GetMapping
    public String showAdminMainPage(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            checkAdminRights(user);
            if (user.isAdmin() || user.getId() == 1) {
                model.addAttribute("admin", user);
                return "Admin_Mode";
            } else
                return "My_Profile";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/users")
    public String showAdminUserPage(@ModelAttribute("userFilterOptions") UserFilterDto userFilterDto,
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
                Long usersCount = userService.allUsersCount();
                model.addAttribute("user", user);
                model.addAttribute("usersCount", usersCount);
                model.addAttribute("filterUsers", userFilterDto);
                model.addAttribute("users", users);

                return "Admin_AllUsers";
            }
            return "My_Profile";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/playlists")
    public String showAllPlaylistPage(@ModelAttribute("playlistFilterOptions")
                                      PlaylistDisplayFilterDto playlistDisplayFilterDto,
                                      Model model) {
        PlaylistFilterOptions playlistFilterOptions = new PlaylistFilterOptions(
                playlistDisplayFilterDto.getTitle(),
                playlistDisplayFilterDto.getPlaylistTimeFrom(),
                playlistDisplayFilterDto.getPlaylistTimeTo() != 0 ? playlistDisplayFilterDto.getPlaylistTimeTo() : 10000,
                playlistDisplayFilterDto.getGenreName());

        List<Playlist> playlists = playlistService.getAll(playlistFilterOptions);
        Long playlistsCount = playlistService.allPlaylistsCount();

        model.addAttribute("playlists", playlists);
        model.addAttribute("playlistFilterOptions", playlistDisplayFilterDto);
        model.addAttribute("playlistsCount", playlistsCount);
        return "Admin_AllPlaylists";
    }

    @GetMapping("users/view/user/{id}")
    public String showAdminUpdateUserPage(@PathVariable int id,
                                          Model model,
                                          HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            if (populateIsAdmin(session)) {
                User userToManipulate = userService.getById(id);
                model.addAttribute("userToManipulate", userToManipulate);

                return "Admin_Update_User";
            }
            return "Error_Page";
        } catch (EntityNotFoundException | AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("users/view/user/{id}")
    public String deleteUserProfilePhoto(@PathVariable int id, HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            if (admin.isAdmin()) {
                User userToManipulate = userService.getById(id);
                userService.removePhoto(userToManipulate);
                String redirectUrl = String.valueOf(userToManipulate.getId());
                return "redirect:" + redirectUrl;

            } else
                return "Error_Page";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping("users/view/user/{id}/newadmin")
    public String changeAdminRights(@PathVariable int id, HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            if (admin.isAdmin()) {
                User userToManipulate = userService.getById(id);
                userService.addRemoveAdmin(admin, userToManipulate);

                String redirectUrl = "/admin/users/view/user/" + userToManipulate.getId();
                return "redirect:" + redirectUrl;

            } else
                return "Error_Page";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping(value = "users/view/user/{id}/delete", method = RequestMethod.POST)
    public String deleteUserProfile(HttpSession session, @ModelAttribute("userToManipulate") User userToManipulate) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);
            if (admin.isAdmin()) {
                userService.delete(userToManipulate);
                return "redirect:/admin/users";
            } else
                return "Error_Page";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/delete/{id}")
    public String showAdminDeletePage(@PathVariable int id,
                                      Model model, HttpSession session) {
        try {
            if (populateIsAuthenticated(session)) {
                String username = session.getAttribute("currentUser").toString();
                User userToManipulate = userService.getById(id);
                User user = userService.getByUsername(username);
                checkAdminRights(user);
                if (user.isAdmin()) {
                    model.addAttribute("userToManipulate", userToManipulate);
                    model.addAttribute("admin", userToManipulate.isAdmin());

                    return "Admin_Update_User";
                }
                return "Error_Page";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        return "redirect:/auth/login";
    }


    @PostMapping("/{id}/update")
    public String updateUserProfile(@Valid @ModelAttribute("currentUser") UserAdminRightsDto userAdminRightsDto,
                                    @PathVariable int id,
                                    BindingResult bindingResult,
                                    Model model,
                                    HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            checkAdminRights(user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "Error_Page";
        }

        try {
            User userToUpdate = userMapper.fromUserAdminRightsDto(id, userAdminRightsDto);
            userService.update(userToUpdate);
            model.addAttribute("currentUser", userAdminRightsDto);

            return "Admin_Mode";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "Error_Page";

        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "duplicate_email", e.getMessage());

            return "Admin_Mode";
        }
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String deleteUserProfile(@PathVariable int id,
                                    BindingResult bindingResult,
                                    Model model,
                                    HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            checkAdminRights(user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "Error_Page";
        }
        try {
            User userToDelete = userService.getById(id);
            userService.delete(userToDelete);
            /*session.removeAttribute("userToDelete");*/

            return "Admin_Mode";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        }
    }

    private static void checkAdminRights(User userToCheck) {
        if (!userToCheck.isAdmin() && userToCheck.getId() != 1) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
