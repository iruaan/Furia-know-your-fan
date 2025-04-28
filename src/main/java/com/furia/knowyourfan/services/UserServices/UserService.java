package com.furia.knowyourfan.services.UserServices;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;


@Service
public class UserService {

  @Autowired
    private UserRepository userRepository;

    public String register(@RequestParam  String email, String password) {

        // Verifica se o email já está registrado
        if (userRepository.findByEmail(email).isPresent()) {
            return "email-ja-cadastrado";
        }

        // Criptografa a senha
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(password);

        // Cria um novo usuário
        User user = new User();
        user.setEmail(email);
        user.setPassword(encryptedPassword);


        // Salva o usuário no banco de dados
        userRepository.save(user);

        return "success";
    }


}


