package com.example.ridepal.controllers.mvc;

import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.TextLengthException;
import com.example.ridepal.helpers.UserMapper;
import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.UserCreateDto;
import com.example.ridepal.services.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterMvcController {
    private final UserService userService;
    private final UserMapper userMapper;

    public RegisterMvcController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    @GetMapping
    public String showRegisterPage(Model model){
        model.addAttribute("user", new UserCreateDto());
        return "Register";
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
    @PostMapping
    public String CreateUser(@Valid @ModelAttribute("user") UserCreateDto userCreateDto,
                             Model model,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "Register";
        }
        try {
            User user = userMapper.fromUserCreateDto(userCreateDto);
            userService.create(user);
            return "redirect:/auth/login";

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";

        } catch (EntityDuplicateException e) {
            result.rejectValue("username", "duplicate_username", e.getMessage());
            return "Register";

        } catch (TextLengthException e) {
            result.rejectValue("firstName", "invalid_length", e.getMessage());
            return "Register";
        }
    }
}
