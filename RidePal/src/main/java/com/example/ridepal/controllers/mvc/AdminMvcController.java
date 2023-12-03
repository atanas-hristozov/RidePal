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


    /*@GetMapping()
    public List<UserDisplayDto> getAllUsers(HttpSession httpSession,
                                            @RequestParam(required = false) String username,
                                            @RequestParam(required = false) String email,
                                            @RequestParam(required = false) String firstName) {
        try {
            User userToCheck = authenticationHelper.tryGetCurrentUser(httpSession);
            checkAdminRights(userToCheck);
            List<UserDisplayDto> users = new ArrayList<>();
            UserFilterOptions userFilterOptionsForAdmins = new UserFilterOptions(username,
                    email, firstName);
            for (User user : userService.getAllByFilterOptions(userFilterOptionsForAdmins)) {
                users.add(userMapper.fromUser(user));
            }
            return users;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }*/


    @GetMapping("/update/{id}")
    public String showAdminUpdatePage(@PathVariable int id,
                                      Model model, HttpSession session) {
        try {
            if (populateIsAuthenticated(session)) {
                String username = session.getAttribute("currentUser").toString();
                User userToManipulate = userService.getById(id);
                User user = userService.getByUsername(username);
                checkAdminRights(user);
                if (user.isAdmin()) {
                    model.addAttribute("userToManipulate", userToManipulate);
                    model.addAttribute("admin", user);

                    return "Admin_UpdateDelete_Users";
                }
                return "Error_Page";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        return "redirect:/auth/login";
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

                    return "Admin_UpdateDelete_Users";
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