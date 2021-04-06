package com.panpawelw.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.panpawelw.weightliftinglog.services.UserService;

@Controller
public class AdminController {

  private final UserService userService;

  public AdminController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  @RequestMapping("/admin")
  public String admin(Model model) {
    String adminName = userService.getLoggedInUserName();
    model.addAttribute("adminName", adminName);
    model.addAttribute("page", "fragments.html :: admin-panel");
    return "home";
  }
}