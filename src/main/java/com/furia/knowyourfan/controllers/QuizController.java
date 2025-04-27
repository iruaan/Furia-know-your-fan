package com.furia.knowyourfan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.furia.knowyourfan.model.QuizQuestion;
import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private HttpSession session;

    @Autowired
    private UserRepository userRepository;

    private final List<QuizQuestion> perguntas = List.of(
        new QuizQuestion("Quem é o CEO da FURIA?", List.of("Jaime Pádua", "Gabriel Fallen", "Gaules", "Jovirone"), "Jaime Pádua"),
        new QuizQuestion("Qual streamer é parceiro da FURIA?", List.of("Xarola", "Alanzoka", "Baiano", "Cerol"), "Xarola"),
        new QuizQuestion("Em qual jogo a FURIA foi semifinalista do IEM Rio Major?", List.of("CS:GO", "Valorant", "LoL", "Free Fire"), "CS:GO")
     
    );

    @GetMapping
    public String mostrarQuiz(Model model) {
        model.addAttribute("perguntaAtual", 0);
        model.addAttribute("pergunta", perguntas.get(0));
        session.setAttribute("quizXp", 0); // reseta o xp do quiz
        return "quiz";
    }

    @PostMapping
    public String responderPergunta(@RequestParam int perguntaIndex, @RequestParam String resposta, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Integer xp = (Integer) session.getAttribute("quizXp");
        if (xp == null) xp = 0;

        QuizQuestion perguntaAtual = perguntas.get(perguntaIndex);

        System.out.println("▶️ Resposta correta esperada: [" + perguntaAtual.getRespostaCorreta() + "]");
        System.out.println("🎯 Resposta recebida do form: [" + resposta + "]");

        if (perguntaAtual.getRespostaCorreta().trim().equalsIgnoreCase(resposta.trim())) {
            xp++;
            session.setAttribute("quizXp", xp);
            System.out.println("✅ Resposta correta. XP do quiz agora: " + xp);
        } else {
            System.out.println("❌ Resposta errada.");
        }

        if (perguntaIndex + 1 >= perguntas.size()) {
            Integer userXp = user.getXp();
            if (userXp == null) userXp = 0;

            System.out.println("📌 XP atual do usuário: " + userXp);
            System.out.println("📈 XP ganho no quiz: " + xp);

            user.setXp(userXp + xp);
            userRepository.save(user);

            System.out.println("✅ XP atualizado para: " + user.getXp());

            model.addAttribute("xp", xp);
            model.addAttribute("user", user);
            return "quizFinal";
        }

        model.addAttribute("perguntaAtual", perguntaIndex + 1);
        model.addAttribute("pergunta", perguntas.get(perguntaIndex + 1));
        return "quiz";
    }
}
