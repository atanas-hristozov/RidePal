package com.example.ridepal.controllers.mvc;

import com.example.ridepal.helpers.PlaylistMapper;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.PlaylistDisplayDto;
import com.example.ridepal.services.PlaylistServiceImpl;
import com.example.ridepal.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/playlists")
public class AllPlaylistsMvcController {
    private final UserService userService;
    private final PlaylistServiceImpl playlistService;
    private final PlaylistMapper playlistMapper;

    public AllPlaylistsMvcController(UserService userService, PlaylistServiceImpl playlistService, PlaylistMapper playlistMapper) {
        this.userService = userService;
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
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
    public String showAllPlaylistPage(Model model, HttpSession session, PlaylistFilterOptions playlistFilterOptions){
        List<Playlist> playlist = playlistService.getAll(playlistFilterOptions);
        model.addAttribute("playlist", playlist);
        return "All_Playlists";
    }
}
