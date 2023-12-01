package com.example.ridepal.controllers.mvc;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.PlaylistMapper;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.PlaylistGenerateDto;
import com.example.ridepal.services.contracts.PlaylistService;
import com.example.ridepal.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/generate")
public class PlaylistGenerateMvcController {
    private final UserService userService;
    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;
    private final AuthenticationHelper authenticationHelper;

    public PlaylistGenerateMvcController(UserService userService,
                                         PlaylistService playlistService,
                                         PlaylistMapper playlistMapper,
                                         AuthenticationHelper authenticationHelper) {
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
        if(session.getAttribute("currentUser") != null){
            Object currentUser = session.getAttribute("currentUser");
            User user = userService.getByUsername(currentUser.toString());
            return user.isAdmin();
        }
        return false;
    }
    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) {
        if (populateIsAuthenticated(session)){
            String username = session.getAttribute("currentUser").toString();
            return userService.getByUsername(username);
        }
        return null;
    }
    @GetMapping
    public String showGeneratePlaylistPage(Model model, HttpSession session){
        model.addAttribute("playlistGenerateDto", new PlaylistGenerateDto());
        return "Generate_tracks";
    }

    @PostMapping("/playlist")
    public String generatePlaylist(@ModelAttribute("playlistGenerateDto") PlaylistGenerateDto playlistGenerateDto,
                                   HttpSession session,
                                   Model model) {
        try {
            // Retrieve user information (if needed)
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("user", user);

            model.addAttribute("playlistGenerateDto", playlistGenerateDto);


            // Process the received data from the form
            int travelDuration = playlistGenerateDto.getTravelDuration();
            String genreNames = playlistGenerateDto.getGenreNames();
            // Other necessary data from playlistGenerateDto

            // Create Playlist object from DTO using Mapper
            Playlist playlist = playlistMapper.fromPlaylistGenerateDto(playlistGenerateDto);

            // Call playlistService to create playlist
            playlistService.create(user, playlist, travelDuration, genreNames);

            // Redirect to success page or appropriate view
            return "Generate_tracks"; // Replace with your success page URL

        } catch (EntityNotFoundException e) {
            // Handle exceptions if needed and return appropriate error view
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // Replace with your error page URL
        }
        catch (AuthorizationException e) {
            // Handle exceptions if needed and return appropriate error view
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // Replace with your error page URL
        }
    }
}
