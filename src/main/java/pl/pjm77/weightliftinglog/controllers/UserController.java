package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.services.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

import static pl.pjm77.weightliftinglog.services.UserService.getLoggedInUserName;

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
        return "home";
    }

    @GetMapping("/edituserdetails")
    public String editUserDetails(Model model) {
        String userName = getLoggedInUserName();
        User user = userService.findUserByName(userName);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("topMessage", "Please edit your details...");
        model.addAttribute("buttonText", "Save details");
        model.addAttribute("page", "fragments.html :: edit-user-details");
        return "home";
    }

    @PostMapping("/edituserdetails")
    public String registerPost(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model) {
        model.addAttribute("topMessage", "Please enter your details to register...");
        model.addAttribute("buttonText", "Register");
        model.addAttribute("page", "fragments.html :: edit-user-details");
        if (bindingResult.hasErrors()) {
            if (user.getId() != null) {
                model.addAttribute("topMessage", "Please edit your details...");
                model.addAttribute("buttonText", "Save details");
            }
        } else {
            try {
//                if(!userService.checkCurrentUserPassword(user.getPassword())) {
//                  model.addAttribute
//                          ("passwordsDontMatch", "     The password doesn't match!");
//                  return "home";
//                }
                userService.saveUser(user);
                model.addAttribute("page", "fragments.html :: edit-user-success");
            }catch(DataIntegrityViolationException e){
                model.addAttribute
                        ("emailExists", "    This email already exists in our database!");
            }
        }
        return "home";
    }
}