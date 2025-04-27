package com.furia.knowyourfan.model.Entitys;

import jakarta.persistence.*;

@Entity
public class FanProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jogoFavorito;
    private String jogadorFavorito;
    private String influencerFavorito;
    private Boolean acompanhaAoVivo;


    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private Boolean onboardingConcluido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJogoFavorito() {
        return jogoFavorito;
    }

    public void setJogoFavorito(String jogoFavorito) {
        this.jogoFavorito = jogoFavorito;
    }

    public String getJogadorFavorito() {
        return jogadorFavorito;
    }

    public void setJogadorFavorito(String jogadorFavorito) {
        this.jogadorFavorito = jogadorFavorito;
    }

    public String getInfluencerFavorito() {
        return influencerFavorito;
    }

    public void setInfluencerFavorito(String influencerFavorito) {
        this.influencerFavorito = influencerFavorito;
    }

    public Boolean getAcompanhaAoVivo() {
        return acompanhaAoVivo;
    }

    public void setAcompanhaAoVivo(Boolean acompanhaAoVivo) {
        this.acompanhaAoVivo = acompanhaAoVivo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getOnboardingConcluido() {
        return onboardingConcluido;
    }

    public void setOnboardingConcluido(Boolean onboardingConcluido) {
        this.onboardingConcluido = onboardingConcluido;
    }

}
