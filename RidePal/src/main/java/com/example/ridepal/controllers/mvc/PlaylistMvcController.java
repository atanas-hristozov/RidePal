package com.example.ridepal.controllers.mvc;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.PlaylistMapper;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.PlaylistUpdateDto;
import com.example.ridepal.services.PlaylistServiceImpl;
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

@Controller
@RequestMapping("/playlist/{id}")
public class PlaylistMvcController {
    private final UserService userService;
    private final PlaylistServiceImpl playlistService;
    private final PlaylistMapper playlistMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PlaylistMvcController(UserService userService, PlaylistServiceImpl playlistService, PlaylistMapper playlistMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
        this.authenticationHelper = authenticationHelper;
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
    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String username = session.getAttribute("currentUser").toString();
            return userService.getByUsername(username);
        }
        return null;
    }

    @ModelAttribute("isAuthor")
    public boolean populateIsAuthor(@PathVariable int id, HttpSession session) {
        User user = authenticationHelper.tryGetCurrentUser(session);
        Playlist playlist = playlistService.getById(id);
        return playlist.getUser().getId() == user.getId();
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping()
    public String showPlaylistPage(@PathVariable int id, Model model, HttpSession session) {
        Playlist playlist = playlistService.getById(id);
        model.addAttribute("playlist", playlist);
        model.addAttribute("musicList", playlist.getTracks());
        return "Playlist";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deletePlaylist(@PathVariable int id,
                                 Model model,
                                 HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            try {
                Playlist playlistToDelete = playlistService.getById(id);
                playlistService.delete(user, playlistToDelete);

                return "redirect:/user";

            } catch (EntityNotFoundException e) {
                model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
                model.addAttribute("error", e.getMessage());
                return "Error_Page";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    @GetMapping("/update")
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

    @PostMapping("/update")
    public String updatePlaylistGenre(@Valid @ModelAttribute ("playlistUpdateDto") PlaylistUpdateDto playlistUpdateDto,
                                      @PathVariable int id,
                                      BindingResult bindingResult,
                                      Model model,
                                      HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);

            if (bindingResult.hasErrors()) {
                return "Edit_User";
            }
            try {
                Playlist updatedPlaylist = playlistMapper.fromPlaylistUpdateDto(playlistUpdateDto, id);
                playlistService.update(user, updatedPlaylist);

                String redirectUrl = "/playlist/" + updatedPlaylist.getId() + "/update";
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
}
