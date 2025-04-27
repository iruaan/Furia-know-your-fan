package com.furia.knowyourfan.services.TwitterServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class TwitterService {

    private final WebClient webClient;

    public TwitterService(@Value("${twitter.bearer-token}") String bearerToken) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.twitter.com/2")
                .defaultHeader("Authorization", "Bearer " + bearerToken)
                .build();
    }

    // Buscar o ID do usuário pelo nome de usuário (@nome)
    public String buscarUserId(String username) {
        try {
            JsonNode response = webClient.get()
                .uri("/users/by/username/{username}", username)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            if (response != null && response.has("data") && response.get("data").has("id")) {
                return response.get("data").get("id").asText();
            } else {
                System.out.println("Resposta inesperada do Twitter: " + response);
                throw new RuntimeException("Usuário não encontrado no Twitter.");
            }

        } catch (WebClientResponseException.TooManyRequests e) {
            System.out.println("Erro 429: Rate limit excedido. Aguarde e tente novamente.");
            throw new RuntimeException("Rate limit excedido. Tente mais tarde.", e);
        } catch (WebClientResponseException e) {
            System.out.println("Erro ao chamar Twitter API: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro ao chamar Twitter API: " + e.getStatusCode(), e);
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao chamar Twitter API.", e);
        }
    }

    // Buscar tweets recentes de um usuário específico
    public String buscarTweetsRecentes(String userId) {
        try {
            return webClient.get()
                    .uri("/users/{id}/tweets", userId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("Erro ao buscar tweets: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro ao buscar tweets do usuário.", e);
        } catch (Exception e) {
            System.out.println("Erro inesperado ao buscar tweets: " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao buscar tweets.", e);
        }
    }

    // Verificar se o usuário fez um tweet com a hashtag #FuriaFan
    public boolean verificarTweetComHashtag(String username, String hashtag) {
        try {
            // Buscar o userId com base no username
            String userId = buscarUserId(username);

            // Buscar tweets recentes do usuário
            String tweets = buscarTweetsRecentes(userId);

            // Verificar se algum tweet contém a hashtag
            if (tweets != null && tweets.contains(hashtag)) {
                return true; // Encontrou um tweet com a hashtag
            }
            return false; // Não encontrou tweet com a hashtag
        } catch (Exception e) {
            System.out.println("Erro ao verificar tweet com a hashtag: " + e.getMessage());
            throw new RuntimeException("Erro ao verificar tweet com a hashtag.", e);
        }
    }
}
