package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pjm77.weightliftinglog.services.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping("/user")
    public String user(Model model) {

        // Get the name of the user who is currently logged in and check for his admin rights

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName;
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
            authorities = ((UserDetails) principal).getAuthorities();
        } else {
            userName = principal.toString();
        }
        for (GrantedAuthority grantedAuthority : authorities) {
            if (("ROLE_ADMIN").equals(grantedAuthority.getAuthority())) {
                model.addAttribute("adminRights", true);
            }
        }

        // pass the user name to view

        model.addAttribute("userGreeting", "Hello " + userName + "!");
        model.addAttribute("page", "fragments.html :: user-panel");
        System.out.println(userService.findUserByName(userName).getPassword());
        return "home";
    }
}