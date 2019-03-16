package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            model.addAttribute("logoutMessage", "You have logged out successfully!");
        }else{
            model.addAttribute("logoutMessage", "Logout error!");
        }
        model.addAttribute("page", "fragments.html :: login");
        return "home";
    }

    @GetMapping("/register")
    public String registerGet(Model model) {
        User user = new User();
        user.setRole("USER");
        model.addAttribute("user", user);
        model.addAttribute("topMessage", "Please enter your details to register...");
        model.addAttribute("buttonText", "Register");
        model.addAttribute("page", "fragments.html :: edit-user-details");
        return "home";
    }

    @GetMapping("/edituserdetails")
    public String editUserDetails(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        User user = userRepository.findUserByName(userName);
        model.addAttribute("user", user);
        model.addAttribute("topMessage", "Please edit your details...");
        model.addAttribute("buttonText", "Save details");
        model.addAttribute("page", "fragments.html :: edit-user-details");
        return "home";
    }

    @PostMapping("/saveuser")
    public String registerPost(@Valid User user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            if(user.getId()==null) {
                model.addAttribute("topMessage", "Please enter your details to register...");
                model.addAttribute("buttonText", "Register");
            }else {
                model.addAttribute("topMessage", "Please edit your details...");
                model.addAttribute("buttonText", "Save details");
            }
            model.addAttribute("page", "fragments.html :: edit-user-details");
            System.out.println(user.toString());
            return "home";
        }else {
            model.addAttribute("page", "fragments.html :: edit-user-success");
            System.out.println(user.toString());
            return "home";
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping("/user")
    public String user(Model model) {
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
        model.addAttribute("userGreeting", "Hello " + userName + "!");
        model.addAttribute("page", "fragments.html :: user-panel");
        return "home";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/admin")
    public String admin(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String adminName;
        if (principal instanceof UserDetails) {
            adminName = ((UserDetails) principal).getUsername();
        } else {
            adminName = principal.toString();
        }
        model.addAttribute("adminGreeting", "Hello " + adminName + "!");
        model.addAttribute("page", "fragments.html :: admin-panel");
        return "home";
    }
}