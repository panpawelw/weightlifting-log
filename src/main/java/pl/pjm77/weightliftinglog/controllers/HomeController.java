package pl.pjm77.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        return "redirect:/user";
    }

    @RequestMapping("/failure")
    public String failure(Model model) {
        System.out.println("get failure");
        model.addAttribute("loginError", true);
        return "home";
    }

    @GetMapping("/register")
    public String registerGet(Model model) {
        System.out.println("get register");
        return "register";
    }

    @PreAuthorize("hasAnyRole('USER')")
    @RequestMapping("/user")
    public String user(Model model) {
        System.out.println("user");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("userIndicator", "This is " + username + " logged in!");
        return "home";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }
}