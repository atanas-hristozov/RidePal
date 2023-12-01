package com.example.ridepal.controllers.mvc;

import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.User;
import com.example.ridepal.services.PlaylistServiceImpl;
import com.example.ridepal.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/playlist")
public class PlaylistMvcController {
    private final UserService userService;
    private final PlaylistServiceImpl playlistService;
    @Autowired
    public PlaylistMvcController(UserService userService, PlaylistServiceImpl playlistService) {
        this.userService = userService;
        this.playlistService = playlistService;
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
    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
    @GetMapping("/{id}")
    public String showPlaylistPage(@PathVariable int id, Model model, HttpSession session){
        Playlist playlist = playlistService.getById(id);
        model.addAttribute("playlist", playlist);
        model.addAttribute("musicList", playlist.getTracks());
        return "Playlist";
    }
}
