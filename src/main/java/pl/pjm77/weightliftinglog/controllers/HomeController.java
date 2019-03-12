package pl.pjm77.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("showCalc", true);
        model.addAttribute("page", "fragments.html :: login");
        return "home";
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return "redirect:/user";
        } else {
            model.addAttribute("page", "fragments.html :: login");
            return "home";
        }
    }

    @PostMapping("/login")
    public String loginPost() {
        return "redirect:/user";
    }

    @RequestMapping("/loginfailure")
    public String loginFailure(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("page", "fragments.html :: login");
        return "home";
    }

    @RequestMapping("/logout")
    public String logout(Model model){
        System.out.println("Here goes logout!");
        model.addAttribute("page", "fragments.html :: login");
        return "home";
    }

    @RequestMapping("/logoutsuccess")
    public String logoutSuccess(Model model){
        model.addAttribute("page", "fragments.html :: login");
        model.addAttribute("logoutSuccess", true);
        return "home";
    }


    @GetMapping("/register")
    public String registerGet(Model model) {
        model.addAttribute("page", "fragments.html :: register");
        return "home";
    }

    @PostMapping("register")
    public String registerPost(Model model) {
        model.addAttribute("page", "fragments.html :: registersuccess");
        return "home";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping("/user")
    public String user(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            authorities = ((UserDetails) principal).getAuthorities();
        } else {
            username = principal.toString();
        }
        for (GrantedAuthority grantedAuthority : authorities) {
            if (("ROLE_ADMIN").equals(grantedAuthority.getAuthority())) {
                model.addAttribute("adminRights", true);
            }
        }
        model.addAttribute("userIndicator", "Hello " + username + "!");
        model.addAttribute("page", "fragments.html :: userpanel");
        return "home";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("page", "fragments.html :: adminpanel");
        return "home";
    }
}