package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import pl.pjm77.weightliftinglog.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
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
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            model.addAttribute("logoutMessage", "You have logged out successfully!");
        } else {
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
        String userName = getLoggedInUserName();
        User user = userService.findUserByName(userName);
        model.addAttribute("user", user);
        model.addAttribute("topMessage", "Please edit your details...");
        model.addAttribute("buttonText", "Save details");
        model.addAttribute("page", "fragments.html :: edit-user-details");
        return "home";
    }

    @PostMapping("/saveuser")
    public String registerPost(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (user.getId() == null) {
                model.addAttribute("topMessage", "Please enter your details to register...");
                model.addAttribute("buttonText", "Register");
            } else {
                model.addAttribute("topMessage", "Please edit your details...");
                model.addAttribute("buttonText", "Save details");
            }
            model.addAttribute("page", "fragments.html :: edit-user-details");
            System.out.println(user.toString());
            return "home";
        } else {
            model.addAttribute("page", "fragments.html :: edit-user-success");
            userService.saveUser(user);
            return "home";
        }
    }

    /**
     * Provides the name of the user who is currently logged in, or a string representation of principal object if it's
     * anything else than instance of UserDetail
     * @return string value with name of the user or string representation of principal object
     */
    static String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}