package com.furia.knowyourfan.controllers;

import com.furia.knowyourfan.model.Entitys.FanProfile;
import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.services.UserServices.FanProfileService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/onboarding")
public class FanProfileController {

    private final FanProfileService fanProfileService;

    public FanProfileController(FanProfileService fanProfileService) {
        this.fanProfileService = fanProfileService;
    }

    @GetMapping
    public String showOnboarding(HttpServletRequest request, Model model) {
        User usuario = (User) request.getSession().getAttribute("user");

        // se não estiver logado
        if (usuario == null) return "redirect:/login";

        FanProfile perfil = fanProfileService.findByUser(usuario).orElse(new FanProfile());
        model.addAttribute("fanProfile", perfil);
        return "onboarding";
    }

    @PostMapping
    public String submitOnboarding(@RequestBody FanProfile fanProfile, HttpServletRequest request) {
        User usuario = (User) request.getSession().getAttribute("user");
        if (usuario == null) {
            return "redirect:/login";
        }

        fanProfile.setUser(usuario);
        fanProfile.setOnboardingConcluido(true);
        fanProfileService.save(fanProfile);

        return "redirect:/fan";
    }
}
