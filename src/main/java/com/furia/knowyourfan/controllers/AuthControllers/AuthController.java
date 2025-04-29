package com.furia.knowyourfan.controllers.AuthControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.furia.knowyourfan.model.Entitys.FanProfile;
import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;
import com.furia.knowyourfan.services.UserServices.FanProfileService;
import com.furia.knowyourfan.services.UserServices.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FanProfileService fanProfileService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
        String result = loginService.login(email, password);

        if (result.equals("Login bem-sucedido!")) {
            // Buscar o usuário
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                request.getSession().setAttribute("user", user); // Guarda o usuário na sessão
                

                // Verifica se o onboarding está completo
                boolean onboardingConcluido = fanProfileService.findByUser(user)
                        .map(FanProfile::getOnboardingConcluido)
                        .orElse(false);

                if (!onboardingConcluido) {
                    // Redireciona para o onboarding se ainda não completou
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .header("Location", "/onboarding")
                            .build();
                }

                // Se já fez onboarding, vai pra /fan
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", "/fan")
                        .build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}

