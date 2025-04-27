package com.furia.knowyourfan.controllers.QuestsControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.furia.knowyourfan.services.TwitterServices.TwitterService;

@Controller
@RequestMapping("/missao")
public class MissaoController {

    @Autowired
    private TwitterService twitterService;

    @GetMapping
    public String mostrarMissao(Model model) {
        return "missao";
    }

    @PostMapping("/concluir")
    public String concluirMissao(@RequestParam String usuarioTwitter, Model model) {
        String usernameFormatado = usuarioTwitter.replace("@", "").trim();

        // Buscar o ID do usuário
        String userId;
        try {
            userId = twitterService.buscarUserId(usernameFormatado);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao buscar o usuário: " + e.getMessage());
            return "missao";
        }

        if (userId == null) {
            model.addAttribute("erro", "Usuário não encontrado.");
            return "missao";
        }

        // Buscar os tweets recentes do usuário
        String tweets;
        try {
            tweets = twitterService.buscarTweetsRecentes(userId);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao buscar os tweets: " + e.getMessage());
            return "missao";
        }

        // Verificar se algum tweet contém a hashtag #FuriaFan
        if (tweets != null && tweets.contains("#FuriaFan")) {
            model.addAttribute("sucesso", "Missão concluída! Tweet com #FuriaFan encontrado.");
        } else {
            model.addAttribute("erro", "Nenhum tweet com #FuriaFan encontrado.");
        }

        return "missao";
    }
}
