package com.panpawelw.weightliftinglog.controllers;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.VerificationToken;
import com.panpawelw.weightliftinglog.registration.event.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import com.panpawelw.weightliftinglog.validators.RegistrationPasswordValidator;
import com.panpawelw.weightliftinglog.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class HomeController {

    private final UserService userService;
    private final RegistrationPasswordValidator registrationPasswordValidator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public HomeController(UserService userService,
                          RegistrationPasswordValidator registrationPasswordValidator,
                          ApplicationEventPublisher applicationEventPublisher,
                          VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.registrationPasswordValidator = registrationPasswordValidator;
        this.applicationEventPublisher = applicationEventPublisher;
        this.verificationTokenService = verificationTokenService;
    }

    @InitBinder("user")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(registrationPasswordValidator);
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
        model.addAttribute("loginError", "Wrong username or password!");
        model.addAttribute("page", "fragments.html :: login");
        return "home";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (userService.logoutUser(request, response) == null) {
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
                               BindingResult bindingResult, Model model, WebRequest request) {
        model.addAttribute("page", "fragments.html :: register-user");
        if (!bindingResult.hasErrors()) {
            try {
                if (!user.isActivated()) {
                    userService.saveUser(user);
                    applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                            request.getContextPath(), request.getLocale()));
                    model.addAttribute("header", "Registration successful!");
                    model.addAttribute("message",
                            "Confirmation email has been sent to:<br><br>" + user.getEmail() +
                                    "<br><br>Please activate your account within 24 " +
                                    "hours!<br><br>" + "Don't forget to check your spam " +
                                    "folder!<br><br>");
                } else {
                    model.addAttribute("header", "Registration successful!");
                    userService.saveUser(user);
                    model.addAttribute("message",
                        "Your user account has been registered and activated. Enjoy!<br><br>");
                }
                model.addAttribute("page", "fragments.html :: register-user-message");
            } catch (DataIntegrityViolationException e) {
                model.addAttribute
                        ("emailExists", "This email already exists in our database!");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("header", "Registration error!");
                model.addAttribute("message", "There's been a problem sending activation " +
                        "message to your email address. Please contact administrator to rectify " +
                        "this problem.<br><br>");
                model.addAttribute("page", "fragments.html :: register-user-message");
            }
        }
        return "home";
    }
    @GetMapping("/confirm-account")
    public String confirmAccount(@RequestParam("token")String tokenParam, Model model) {
        VerificationToken verificationToken = verificationTokenService.findByToken(tokenParam);
        if (verificationToken !=null) {
            User user = verificationToken.getUser();
            user.setActivated(true);
            userService.saveUserWithoutModifyingPassword(user);
            verificationTokenService.deleteVerificationToken(verificationToken);
            model.addAttribute("page", "fragments.html :: activate-account-success");
        } else {
            model.addAttribute("page", "fragments.html :: activate-account-failure");
        }
        return "home";
    }
}