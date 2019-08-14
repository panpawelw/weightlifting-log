package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.pjm77.weightliftinglog.models.*;
import pl.pjm77.weightliftinglog.security.validator.PasswordValidator;
import pl.pjm77.weightliftinglog.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class HomeController {

    private final UserService userService;
    private final PasswordValidator passwordValidator;

    @Autowired
    public HomeController(UserService userService, PasswordValidator passwordValidator) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
    }

    @InitBinder("user")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(passwordValidator);
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
            model.addAttribute("page", "fragments.html :: logout-success");
        } else {
            model.addAttribute("page", "fragments.html :: logout-failure");
        }
        return "home";
    }

    @GetMapping("/register")
    public String registerGet(Model model) {
        User user = new User();
        user.setRole("USER");
        model.addAttribute("user", user);
        model.addAttribute("page", "fragments.html :: register-user");
        return "home";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model) {
        model.addAttribute("page", "fragments.html :: register-user");
        if (!bindingResult.hasErrors()) {
            try {
                userService.saveUser(user);
                model.addAttribute("page", "fragments.html :: register-user-success");
            } catch (DataIntegrityViolationException e) {
                model.addAttribute
                        ("emailExists", "    This email already exists in our database!");
            }
        }
        return "home";
    }
}