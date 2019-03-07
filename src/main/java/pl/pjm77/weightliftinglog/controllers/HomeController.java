package pl.pjm77.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("isActive", false);
        System.out.println("get");
        return "home";
    }

    @PostMapping("/")
    public String loggedIn(Model model) {
        model.addAttribute("isActive", true);
        System.out.println("post");
        return "home";
    }

    @RequestMapping("/testerror")
    public String testError() {
        return "error";
    }

//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    @RequestMapping("/loggedin/user")
//    public String user() {
//        return"user";
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @RequestMapping("/loggedin/admin")
//    public String admin() {
//        return "admin";
//    }
}