package com.furia.knowyourfan.controllers;



import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RankController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/rank")
    public String getRanking(Model model) {
        List<User> top10 = userRepository.findTop10ByOrderByXpDesc();
    
        // DEBUG: verificar ordem
        top10.forEach(u -> System.out.println(u.getNome() + " - XP: " + u.getXp()));
    
        model.addAttribute("usuarios", top10);
        return "rank";
    }
    
    
}
