package pl.pjm77.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping("/admin")
    public String admin(Model model) {
        String adminName = HomeController.getLoggedInUserName();
        model.addAttribute("adminGreeting", "Hello " + adminName + "!");
        model.addAttribute("page", "fragments.html :: admin-panel");
        return "home";
    }
}
