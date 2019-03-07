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
        model.addAttribute("showCalc", true);
        System.out.println("get anybody");
        return "home";
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        System.out.println("get login");
        return "home";
    }

    @PostMapping("/login")
    public String loginPost(Model model) {
        System.out.println("post login");
        return "test";
    }

    @RequestMapping("/user")
    public String user(Model model) {
        System.out.println("user");
        return "home";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
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