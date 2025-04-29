package com.furia.knowyourfan.controllers.ApiControllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.UserRepository;

import java.io.IOException;
import java.util.*;

@Controller
public class TwitchAuthController {

    @Value("${twitch.client-id}")
    private String clientId;

    @Value("${twitch.client-secret}")
    private String clientSecret;

    @Value("${twitch.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UserRepository usuarioRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/conectar/twitch")
    public void redirectToTwitchAuth(HttpServletResponse response) throws IOException {
        String authUrl = UriComponentsBuilder
                .fromUriString("https://id.twitch.tv/oauth2/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "user:read:follows user:read:email")
                .build()
                .toUriString();

        response.sendRedirect(authUrl);
    }

    @GetMapping("/twitch/callback")
    public String handleTwitchCallback(@RequestParam("code") String code) {
        String tokenUrl = "https://id.twitch.tv/oauth2/token";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("grant_type", "authorization_code")
                .queryParam("redirect_uri", redirectUri);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(builder.toUriString(), null, Map.class);

        if (tokenResponse.getStatusCode() == HttpStatus.OK) {
            String accessToken = (String) tokenResponse.getBody().get("access_token");
            return "redirect:/twitch/follows?token=" + accessToken;
        } else {
            return "redirect:/erro";
        }
    }

@GetMapping("/twitch/follows")
public String pegarFollows(@RequestParam("token") String token, 
                         HttpServletRequest request, 
                         Model model) {
    
    // Recupera o usuário logado da sessão
    User user = (User) request.getSession().getAttribute("user");
    
    // Verifica se o usuário está logado
    if (user == null) {
        return "redirect:/login";
    }

    // Dados do usuário
    String email = user.getEmail();
    String cpf = user.getCpf();
    String nomeCompleto = user.getNome();
    String dataNascimento = user.getDataNascimento();

    // Configuração da chamada à API da Twitch
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.set("Client-Id", clientId);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    // 1. Obter informações do usuário na Twitch
    ResponseEntity<Map> userResponse = restTemplate.exchange(
            "https://api.twitch.tv/helix/users",
            HttpMethod.GET,
            entity,
            Map.class
    );

    Map<String, Object> userData = (Map<String, Object>) ((List) userResponse.getBody().get("data")).get(0);
    String userId = (String) userData.get("id");
    String userName = (String) userData.get("display_name");

    // 2. Obter canais seguidos
    String followsUrl = "https://api.twitch.tv/helix/channels/followed?user_id=" + userId + "&first=100";
    ResponseEntity<Map> followsResponse = restTemplate.exchange(
            followsUrl,
            HttpMethod.GET,
            entity,
            Map.class
    );

    List<Map<String, Object>> follows = (List<Map<String, Object>>) followsResponse.getBody().get("data");

    // 3. Filtrar canais de e-sports
    List<String> canaisEsports = Arrays.asList(
            "FURIAtv", "LOUD", "paiN Gaming", "MIBR", "RED Canids", "INTZ",
            "RiotGames", "RocketLeague", "CBLOL", "lolesports", "LCS", "LEC", "LCK", "LPL"
    );

    List<String> encontrados = new ArrayList<>();
    for (Map<String, Object> follow : follows) {
        String nomeCanal = (String) follow.get("broadcaster_name");
        if (nomeCanal != null) {
            for (String canal : canaisEsports) {
                if (nomeCanal.equalsIgnoreCase(canal)) {
                    encontrados.add(nomeCanal);
                    break;
                }
            }
        }
    }

    // 4. Classificação do fã
    String statusFã;
    if (encontrados.size() >= 4 && encontrados.contains("FURIAtv")) {
        statusFã = "Super fã de e-sports e fã da FURIA!";
    } else if (encontrados.size() >= 2) {
        statusFã = "Fã médio de e-sports.";
    } else {
        statusFã = "Iniciante no mundo dos e-sports.";
    }

    // 5. Armazenar na sessão (IMPORTANTE)
    HttpSession session = request.getSession();
    session.setAttribute("encontrados", encontrados);
    session.setAttribute("statusFã", statusFã);
    session.setAttribute("totalFollows", follows != null ? follows.size() : 0);
    session.setAttribute("userName", userName);

    // 6. Adicionar ao model para a view atual
    model.addAttribute("userName", userName);
    model.addAttribute("totalFollows", follows != null ? follows.size() : 0);
    model.addAttribute("encontrados", encontrados);
    model.addAttribute("nomeCompleto", nomeCompleto);
    model.addAttribute("email", email);
    model.addAttribute("cpf", cpf);
    model.addAttribute("dataNascimento", dataNascimento);
    model.addAttribute("statusFã", statusFã);
    model.addAttribute("token", token); // Mantém o token disponível

    return "twitch-resumo";
}
    

    @GetMapping("/redes-sociais")
    public String redesSociaisPage() {
        return "redes-sociais";
    }
}