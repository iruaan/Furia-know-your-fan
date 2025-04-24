package com.furia.knowyourfan.controllers.AuthControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;
import com.furia.knowyourfan.services.UserServices.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserRepository userRepository;



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
        String result = loginService.login(email, password);

        if (result.equals("Login bem-sucedido!")) {
            // Armazenar o usuário na sessão
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                request.getSession().setAttribute("user", user); // Guarda o usuário na sessão
            }

            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/fan").build(); // Redireciona para a página pós-login
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
