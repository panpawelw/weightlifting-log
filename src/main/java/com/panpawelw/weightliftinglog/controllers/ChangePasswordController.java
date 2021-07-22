package com.panpawelw.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.panpawelw.weightliftinglog.models.ChangePassword;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.validators.ChangePasswordValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.panpawelw.weightliftinglog.misc.Message.prepMessage;

@Controller
public class ChangePasswordController {

  private final ChangePasswordValidator changePasswordValidator;
  private final UserService userService;

  @Autowired
  public ChangePasswordController(ChangePasswordValidator changePasswordValidator,
      UserService userService) {
    this.changePasswordValidator = changePasswordValidator;
    this.userService = userService;
  }

  @InitBinder("changePassword")
  protected void initBinder(final WebDataBinder binder) {
    binder.addValidators(changePasswordValidator);
  }

  @GetMapping("/user/changepassword")
  public String changePasswordGet(Model model) {
    ChangePassword changePassword = new ChangePassword();
    model.addAttribute("changePassword", changePassword);
    model.addAttribute("page", "fragments.html :: change-password");
    return "home";
  }

  @PostMapping("/user/changepassword")
  public String changePasswordPost(
      @Valid @ModelAttribute("changePassword") ChangePassword changePassword, Model model,
      BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("page", "fragments.html :: change-password");
    } else {
      String username;
      try {
        username = userService.findUserByEmail
            (userService.getLoggedInUsersEmail()).getName();
        userService.changeCurrentUserPassword(changePassword.getNewPassword());
      } catch (Exception e) {
        prepMessage(model, "", "Error!", "There's been a database error!");
        return "home";
      }
      userService.logoutUser(request, response);
      userService.autoLogin(request, username, changePassword.getNewPassword());
      model.addAttribute("page", "fragments.html :: change-password-success");
    }
    return "home";
  }
}