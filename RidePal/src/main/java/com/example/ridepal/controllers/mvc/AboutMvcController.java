package com.example.ridepal.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutMvcController {
    @GetMapping
    public String showAboutPage(Model model, HttpSession session){
        return "About";
    }
}
