package com.furia.knowyourfan.controllers.AuthControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import com.furia.knowyourfan.services.UserServices.UserService;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestParam String name, 
                               @RequestParam String nickname, 
                               @RequestParam String email, 
                               @RequestParam String password, 
                               @RequestParam String birthDate) {

        // Chama o serviço para registrar o usuário
        String result = userService.register(name, nickname, email, password, birthDate);

        // Caso o email já esteja registrado, redireciona com erro
        if (result.equals("email-ja-cadastrado")) {
            return "redirect:/register?error=email-ja-cadastrado";
        }

        // Caso o cadastro seja bem-sucedido, redireciona para login
        return "redirect:/login";
    }


}
