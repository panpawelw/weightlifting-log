package pl.pjm77.weightliftinglog.controllers;

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
import pl.pjm77.weightliftinglog.models.*;
import pl.pjm77.weightliftinglog.registration.event.OnRegistrationCompleteEvent;
import pl.pjm77.weightliftinglog.services.VerificationTokenService;
import pl.pjm77.weightliftinglog.validators.RegistrationPasswordValidator;
import pl.pjm77.weightliftinglog.services.UserService;

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
                if (user.isEmailReal()) {
                    user.setEnabled(false);
                    userService.saveUser(user);
                    applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                            request.getContextPath(), request.getLocale()));
                    model.addAttribute("emailSent",
                            "Confirmation email has been sent to:<br><br>" + user.getEmail() +
                                    "<br><br>Please activate your account within 24 hours!<br><br>");
                } else {
                    userService.saveUser(user);
                }
                model.addAttribute("page", "fragments.html :: register-user-success");
            } catch (DataIntegrityViolationException e) {
                model.addAttribute
                        ("emailExists", "This email already exists in our database!");
            } catch (Exception e) {
                System.out.println("Email error!");
            }
        }
        return "home";
    }
    @GetMapping("/confirm-account")
    public String confirmAccount(@RequestParam("token")String tokenParam, Model model) {
        VerificationToken verificationToken = verificationTokenService.findByToken(tokenParam);
        if (verificationToken !=null) {
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userService.saveUserWithoutModifyingPassword(user);
            verificationTokenService.deleteVerificationToken(verificationToken);
            model.addAttribute("page", "fragments.html :: activate-account-success");
        } else {
            model.addAttribute("page", "fragments.html :: activate-account-failure");
        }
        return "home";
    }
}