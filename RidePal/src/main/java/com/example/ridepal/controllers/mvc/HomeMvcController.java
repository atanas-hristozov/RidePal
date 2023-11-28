package com.example.ridepal.controllers.mvc;

import com.example.ridepal.models.User;
import com.example.ridepal.services.contracts.PlaylistService;
import com.example.ridepal.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final UserService userService;
    private final PlaylistService playlistService;
    @Autowired
    public HomeMvcController(UserService userService, PlaylistService playlistService) {
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
    @GetMapping
    public String showHomePage(Model model, HttpSession session){

        return "Index";
    }
}
