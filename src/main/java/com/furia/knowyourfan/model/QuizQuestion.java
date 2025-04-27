package com.furia.knowyourfan.model;

import java.util.List;

public class QuizQuestion {
    private String pergunta;
    private List<String> opcoes;
    private String respostaCorreta;
    public String getPergunta() {
        return pergunta;
    }
    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }
    public List<String> getOpcoes() {
        return opcoes;
    }
    public void setOpcoes(List<String> opcoes) {
        this.opcoes = opcoes;
    }
    public String getRespostaCorreta() {
        return respostaCorreta;
    }
    public void setRespostaCorreta(String respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }
    public QuizQuestion(String pergunta, List<String> opcoes, String respostaCorreta) {
        this.pergunta = pergunta;
        this.opcoes = opcoes;
        this.respostaCorreta = respostaCorreta;
    }

    // Construtor, getters e setters
}
