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
import com.example.ridepal.models.dtos.PlaylistDisplayFilterDto;
import com.example.ridepal.models.dtos.UserCreateUpdatePhotoDto;
import com.example.ridepal.models.dtos.UserUpdateDto;
import com.example.ridepal.services.PlaylistServiceImpl;
import com.example.ridepal.services.contracts.UserService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final PlaylistServiceImpl playlistService;

    @Autowired
    public UserMvcController(UserService userService,
                             AuthenticationHelper authenticationHelper,
                             UserMapper userMapper,
                             PlaylistServiceImpl playlistService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.playlistService = playlistService;
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
    public String showUserPage(Model model, PlaylistDisplayFilterDto playlistDisplayFilterDto, HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            User user = userService.getByUsername(username);

            PlaylistFilterOptions playlistFilterOptions = new PlaylistFilterOptions(
                    playlistDisplayFilterDto.getTitle(),
                    playlistDisplayFilterDto.getPlaylistTimeFrom(),
                    playlistDisplayFilterDto.getPlaylistTimeTo() != 0 ? playlistDisplayFilterDto.getPlaylistTimeTo() : 10000,
                    playlistDisplayFilterDto.getGenreName());

            List<Playlist> playlists = playlistService.getAll(playlistFilterOptions);
            model.addAttribute("user", user);
            model.addAttribute("userProfilePicture", user.getUserPhoto());
            model.addAttribute("playlists", playlists);
            return "My_Profile";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/selectedPhoto")
    public ResponseEntity<byte[]> getPhoto(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);

            // User is authenticated, proceed to retrieve the photo
            byte[] userPhoto = user.getUserPhoto();

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


   /* @GetMapping("/selectedPhoto/{id}")
    public ResponseEntity<byte[]> getPhotoId(@PathVariable int id, HttpSession session) {
        try {
            // Retrieve user details based on the provided user ID
            User user = userService.getById(id); // Replace with your logic to fetch the user by ID

            byte[] userPhoto = user.getUserPhoto();

            if (userPhoto != null && userPhoto.length > 0) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userPhoto);
            } else {
                // If no user photo is found, return a default image
                InputStream defaultImageStream = getClass().getResourceAsStream("/static/default-user-photo.jpg");
                byte[] defaultImage = IOUtils.toByteArray(defaultImageStream);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(defaultImage);
            }
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/


    @GetMapping("/update")
    public String showEditUserPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            UserUpdateDto userUpdateDto = userMapper.fromUserToUserUpdate(user);
            model.addAttribute("currentUser", userUpdateDto);

            return "Edit_User";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
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
                                           UserCreateUpdatePhotoDto userCreateUpdatePhotoDto) {
        try {
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            User user = userMapper.fromUserCreateUpdatePhotoDto(loggedUser.getId(), userCreateUpdatePhotoDto);
            model.addAttribute("currentUser", user);

            return "Upload_photo";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/photo")
    public String updateUserProfilePhoto(@Valid @ModelAttribute("currentUser")
                                         @RequestParam("file") MultipartFile file,
                                         RedirectAttributes redirectAttributes,
                                         UserCreateUpdatePhotoDto userCreateUpdatePhotoDto,
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
            userService.uploadPhoto(user, file);
            model.addAttribute("currentUser", user);
            redirectAttributes.addFlashAttribute("successMessage", "Photo uploaded successfully");
            return "redirect:/user";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading photo");
        }
        return "Edit_User";
    }

    @PostMapping
    public String deleteUserProfilePhoto(HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            userService.removePhoto(user);

            return "redirect:/user";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
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
}
