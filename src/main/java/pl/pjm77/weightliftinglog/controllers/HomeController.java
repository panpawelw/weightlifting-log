package pl.pjm77.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.services.MyUserDetailsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Controller
public class HomeController {

    private MyUserDetailsService myUserDetailsService;

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

    @RequestMapping("/failure")
    public String failure(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("page", "fragments.html :: login");
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