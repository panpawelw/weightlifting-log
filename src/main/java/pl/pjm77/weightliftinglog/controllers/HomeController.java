package pl.pjm77.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/")
    public String loggedIn() {
        return "home";
    }

    @PreAuthorize("hasAnyRole('USER')")
    @RequestMapping("/loggedin/user")
    public String user() {
        return"user";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/loggedin/admin")
    public String admin() {
        return "admin";
    }
}