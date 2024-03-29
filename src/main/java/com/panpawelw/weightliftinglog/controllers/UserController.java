package com.panpawelw.weightliftinglog.controllers;

import static com.panpawelw.weightliftinglog.controllers.misc.Message.prepMessage;

import com.panpawelw.weightliftinglog.exceptions.ApiRequestException;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import com.panpawelw.weightliftinglog.validators.UpdateDetailsPasswordValidator;
import com.panpawelw.weightliftinglog.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserController {

  private final UserService userService;
  private final WorkoutService workoutService;
  private final UpdateDetailsPasswordValidator updateDetailsPasswordValidator;
  private final VerificationTokenService verificationTokenService;

  @Autowired
  public UserController(UserService userService, WorkoutService workoutService,
                        UpdateDetailsPasswordValidator updateDetailsPasswordValidator,
                        VerificationTokenService verificationTokenService) {
    this.userService = userService;
    this.workoutService = workoutService;
    this.updateDetailsPasswordValidator = updateDetailsPasswordValidator;
    this.verificationTokenService = verificationTokenService;
  }

  @InitBinder("user")
  protected void initBinder(final WebDataBinder binder) {
    binder.addValidators(updateDetailsPasswordValidator);
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @RequestMapping("/user")
  public String user(Model model, HttpServletRequest request, HttpServletResponse response) {
    User user;
    try {
      user = userService.findUserByEmail(userService.getLoggedInUsersEmail());
      if (user == null) {
        prepMessage(model, "/weightliftinglog", "Error!", "Can't retrieve user!");
        return "home";
      }
      if (!user.isActivated()) {
        userService.logoutUser(request, response);
        model.addAttribute("loginError", verificationTokenService.removeAccountIfTokenExpired(user));
        model.addAttribute("page", "fragments.html :: login");
        return "home";
      }
    } catch (HibernateException e) {
      throw new ApiRequestException("There's a problem with database connection!");
    }
    model.addAttribute("userName", user.getName());
    model.addAttribute("adminRights", userService.checkLoggedInUserForAdminRights());
    model.addAttribute("page", "fragments.html :: user-panel");
    model.addAttribute("userPanelPage", "fragments.html :: user-panel-default");
    model.addAttribute("workouts", workoutService.findWorkoutsByUser(user));
    return "home";
  }

  @GetMapping("/user/update")
  public String editUserDetailsGet(Model model) {
    User user;
    try {
      user = userService.findUserByEmail(userService.getLoggedInUsersEmail());
      if (user == null) {
        prepMessage(model, "/weightliftinglog/user", "Error!", "User doesn't exist!");
        return "home";
      }
    } catch (HibernateException e) {
      throw new ApiRequestException("There's a problem with database connection!");
    }
    user.setPassword("");
    model.addAttribute("user", user);
    model.addAttribute("page", "fragments.html :: update-user");
    return "home";
  }

  @PostMapping("/user/update")
  public String editUserDetailsPost(@Valid @ModelAttribute("user") User user,
                                    BindingResult bindingResult, Model model,
                                    HttpServletRequest request, HttpServletResponse response) {
    model.addAttribute("page", "fragments.html :: update-user");
    if (!bindingResult.hasErrors()) {
      try {
        String password = user.getPassword();
        userService.saveUserWithoutModifyingPassword(user);
        userService.logoutUser(request, response);
        userService.autoLogin(request, user.getName(), password);
        prepMessage(model, "/weightliftinglog/user", "Success!",
            "User details have been updated!");
      } catch (DataIntegrityViolationException e) {
        if(e.getMostSpecificCause().getMessage().contains("user_unique_name_idx")) {
          bindingResult.rejectValue("name", "error.name",
              "This username has already been taken!");
        }
        if(e.getMostSpecificCause().getMessage().contains("user_unique_email_idx")) {
          bindingResult.rejectValue("email", "error.email",
              "This email already exists in our database!");
        }
      } catch (Exception e) {
        prepMessage(model, "", "Error!",
            "There's a problem with database connection!");
      }
    }
    return "home";
  }
}