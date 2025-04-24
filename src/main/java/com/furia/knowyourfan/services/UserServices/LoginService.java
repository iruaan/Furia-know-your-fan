package com.furia.knowyourfan.services.UserServices;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String login(String email, String password) {
        // Busca o usuário pelo email
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "Usuário não encontrado.";
        }

        // Verifica se a senha fornecida corresponde à senha criptografada no banco
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Senha incorreta.";
        }

        // Login bem-sucedido
        return "Login bem-sucedido!";
    }
}
