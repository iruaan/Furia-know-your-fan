package com.furia.knowyourfan.controllers.ApiControllers;

import com.furia.knowyourfan.model.Entitys.FanProfile;
import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.services.UserServices.FanProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TwitchAnalysisController {

    @Autowired
    private FanProfileService fanProfileService;

    @PostMapping("/verificar-compatibilidade")
    public String verificarCompatibilidade(
            @RequestParam("url") String url,
            @RequestParam("token") String token,
            HttpServletRequest request,
            Model model) {

        // 1. Recupera os dados da sessão
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<String> encontrados = (List<String>) session.getAttribute("encontrados");
        if (encontrados == null || encontrados.isEmpty()) {
            model.addAttribute("error", "Nenhum canal para comparar");
            return "twitch-resumo";
        }

        // 2. Carrega o perfil do fã
        FanProfile fanProfile = fanProfileService.findByUser(user)
                .orElse(new FanProfile()); // Perfil vazio se não existir

        // 3. Verificação simplificada (apenas na URL)
        Map<String, Boolean> resultados = new LinkedHashMap<>();
        String urlLower = url.toLowerCase();

        // Verifica canais Twitch na URL
        for (String canal : encontrados) {
            resultados.put("Canal: " + canal, urlLower.contains(canal.toLowerCase()));
        }

        // Verifica dados do onboarding na URL
        if (fanProfile.getJogoFavorito() != null && !fanProfile.getJogoFavorito().isEmpty()) {
            resultados.put("Jogo Favorito: " + fanProfile.getJogoFavorito(),
                    urlLower.contains(fanProfile.getJogoFavorito().toLowerCase()));
        }

        if (fanProfile.getInfluencerFavorito() != null && !fanProfile.getInfluencerFavorito().isEmpty()) {
            resultados.put("Influencer: " + fanProfile.getInfluencerFavorito(),
                    urlLower.contains(fanProfile.getInfluencerFavorito().toLowerCase()));
        }

        if (fanProfile.getJogadorFavorito() != null && !fanProfile.getJogadorFavorito().isEmpty()) {
            resultados.put("Jogador: " + fanProfile.getJogadorFavorito(),
                    urlLower.contains(fanProfile.getJogadorFavorito().toLowerCase()));
        }

        // 4. Calcula porcentagem (apenas para canais)
        long matchesCanais = encontrados.stream()
                .filter(canal -> resultados.get("Canal: " + canal))
                .count();
        int porcentagem = (int) ((double) matchesCanais / encontrados.size() * 100);

        // 5. Adiciona resultados ao model
        model.addAttribute("resultadoCompatibilidade", resultados);
        model.addAttribute("urlAnalisada", url);
        model.addAttribute("porcentagemCompatibilidade", porcentagem);
        model.addAttribute("fanProfile", fanProfile);
        
        // 6. Mantém todos os atributos necessários
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("encontrados", encontrados);
        model.addAttribute("statusFã", session.getAttribute("statusFã"));
        model.addAttribute("totalFollows", session.getAttribute("totalFollows"));
        model.addAttribute("token", token);

        return "twitch-resumo";
    }
}