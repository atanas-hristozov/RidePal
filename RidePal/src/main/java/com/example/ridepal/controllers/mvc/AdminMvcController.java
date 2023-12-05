package com.example.ridepal.controllers.mvc;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.PlaylistMapper;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.*;
import com.example.ridepal.services.contracts.PlaylistService;
import com.example.ridepal.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.List;

import static com.example.ridepal.services.UserServiceImpl.ERROR_MESSAGE;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {

    private final UserService userService;
    private final PlaylistService playlistService;
    private final UserMapper userMapper;
    private final PlaylistMapper playlistMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AdminMvcController(UserService userService,
                              PlaylistService playlistService, UserMapper userMapper,
                              PlaylistMapper playlistMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.playlistService = playlistService;
        this.userMapper = userMapper;
        this.playlistMapper = playlistMapper;
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

    @GetMapping("users/view/user/{id}")
    public String showAdminUpdateUserPage(@PathVariable int id,
                                          Model model,
                                          HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            if (populateIsAdmin(session)) {
                User userToManipulate = userService.getById(id);
                model.addAttribute("userToManipulate", userToManipulate);
                model.addAttribute("userProfilePicture", userToManipulate.getUserPhoto());

                return "Admin_Update_User";
            }
            return "Error_Page";
        } catch (EntityNotFoundException | AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("users/view/user/{id}/selectedPhoto")
    public ResponseEntity<byte[]> getPhoto(@PathVariable int id, HttpSession session) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);
            checkAdminRights(admin);
            User userToManipulate = userService.getById(id);
            // User is authenticated, proceed to retrieve the photo
            byte[] userPhoto = userToManipulate.getUserPhoto();

            if (userPhoto != null && userPhoto.length > 0) {
                // If the user has a photo, return it
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userPhoto);
            } else {
                // If the user doesn't have a photo, return a default image or a placeholder image
                // Replace the following line with your actual path to a default image
                InputStream defaultImageStream = getClass().getResourceAsStream("/static/default-user-photo.jpg");
                byte[] defaultImage = IOUtils.toByteArray(defaultImageStream);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(defaultImage);
            }
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

/*    @GetMapping("/delete/{id}")
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
    }*/


   /* @PostMapping("/{id}/update")
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
    }*/

   /* @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
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
            *//*session.removeAttribute("userToDelete");*//*

            return "Admin_Mode";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error_Page";
        }
    }*/

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


    @RequestMapping(value = "playlists/{id}/delete", method = RequestMethod.POST)
    public String deletePlaylist(@PathVariable int id,
                                 Model model,
                                 HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetCurrentUser(session);
            try {
                Playlist playlistToDelete = playlistService.getById(id);
                playlistService.delete(admin, playlistToDelete);

                return "redirect:/admin/playlists";

            } catch (EntityNotFoundException e) {
                model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
                model.addAttribute("error", e.getMessage());
                return "Error_Page";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("playlists/{id}/update")
    public String showUpdatePlaylistGenre(@PathVariable int id,
                                          Model model,
                                          HttpSession session) {

        try {
            authenticationHelper.tryGetCurrentUser(session);
            try {
                Playlist playlist = playlistService.getById(id);
                PlaylistUpdateDto playlistUpdateDto = playlistMapper.fromPlaylistToPlaylistUpdateDto(playlist);
                model.addAttribute("playlist", playlist);
                model.addAttribute("playlistUpdateDto", playlistUpdateDto);
                return "Edit_Playlist";

            } catch (EntityNotFoundException e) {
                model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
                model.addAttribute("error", e.getMessage());
                return "Error_Page";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("playlists/{id}/update")
    public String updatePlaylistGenre(@Valid @ModelAttribute ("playlistUpdateDto") PlaylistUpdateDto playlistUpdateDto,
                                      @PathVariable int id,
                                      BindingResult bindingResult,
                                      Model model,
                                      HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetCurrentUser(session);

            if (bindingResult.hasErrors()) {
                return "Edit_User";
            }
            try {
                Playlist updatedPlaylist = playlistMapper.fromPlaylistUpdateDto(playlistUpdateDto, id);
                playlistService.update(admin, updatedPlaylist);

                String redirectUrl = "/admin/playlists/" + updatedPlaylist.getId() + "/update";
                return "redirect:" + redirectUrl;

            } catch (EntityNotFoundException e) {
                model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
                model.addAttribute("error", e.getMessage());
                return "Error_Page";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    private static void checkAdminRights(User userToCheck) {
        if (!userToCheck.isAdmin() && userToCheck.getId() != 1) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}

