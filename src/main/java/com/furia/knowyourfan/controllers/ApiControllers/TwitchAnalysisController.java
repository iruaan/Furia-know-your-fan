package com.furia.knowyourfan.controllers.ApiControllers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class TwitchAnalysisController {

    // ... (outros métodos existentes)

    @PostMapping("/verificar-compatibilidade")
    public String verificarCompatibilidade(
            @RequestParam("url") String url,
            @RequestParam("token") String token,
            HttpServletRequest request,
            Model model) {

        // 1. Recupera os dados da sessão
        HttpSession session = request.getSession();
        List<String> encontrados = (List<String>) session.getAttribute("encontrados");
        
        if (encontrados == null || encontrados.isEmpty()) {
            model.addAttribute("error", "Nenhum canal para comparar");
            return "twitch-resumo";
        }

        // 2. Verificação simplificada (apenas com o nome do canal na URL)
        Map<String, Boolean> resultados = new LinkedHashMap<>();
        String urlLower = url.toLowerCase();
        
        for (String canal : encontrados) {
            resultados.put(canal, urlLower.contains(canal.toLowerCase()));
        }

        // 3. Calcula porcentagem
        long matches = resultados.values().stream().filter(b -> b).count();
        int porcentagem = (int) ((double) matches / encontrados.size() * 100);

        // 4. Adiciona resultados ao model
        model.addAttribute("resultadoCompatibilidade", resultados);
        model.addAttribute("urlAnalisada", url);
        model.addAttribute("porcentagemCompatibilidade", porcentagem);
        
        // 5. Mantém todos os atributos necessários
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("encontrados", encontrados);
        model.addAttribute("statusFã", session.getAttribute("statusFã"));
        model.addAttribute("totalFollows", session.getAttribute("totalFollows"));
        model.addAttribute("token", token);

        return "twitch-resumo";
    }

    private Map<String, Boolean> verificarCorrespondencias(String url, List<String> canais) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();
        
        String pageText = doc.text().toLowerCase();
        Map<String, Boolean> resultados = new LinkedHashMap<>();
        
        for (String canal : canais) {
            resultados.put(canal, pageText.contains(canal.toLowerCase()));
        }
        
        return resultados;
    }
}