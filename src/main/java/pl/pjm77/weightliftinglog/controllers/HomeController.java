package pl.pjm77.weightliftinglog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String Home() {
        return "home";
    }

    @RequestMapping("/admin")
    public String Admin() {
        return "admin";
    }
}