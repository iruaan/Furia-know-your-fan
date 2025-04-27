package com.furia.knowyourfan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {


    @GetMapping("/home")
    public String home() {
        return "home"; // Isso retorna templates/home.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @GetMapping("/fan")
    public String getMethodName() {
        return "fan";
    }
    



}
