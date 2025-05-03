# 🎮 Know Your Fan - Projeto de Identificação e Engajamento de Fãs de eSports
Este projeto é uma aplicação full stack com o objetivo de identificar, mapear e classificar fãs de eSports (com foco em organizações como a FURIA), oferecendo uma experiência de onboarding e coleta de dados através de múltiplas fontes como redes sociais, documentos e hábitos de interação digital.

# 🚀 Tecnologias Utilizadas
Java 17 + Spring Boot

Thymeleaf (para renderização de páginas web)

Supabase (banco de dados e autenticação)

API do Twitter

API da Twitch

Tesseract OCR (extração de dados de documentos)


# 🧠 Funcionalidades
Coleta automática de dados pessoais a partir de documento com uso de IA (OCR).

Integração com redes sociais para mapear nível de engajamento com organizações de eSports.

Interface de quiz/onboarding estilo "pergunta por vez", fluida e gamificada.

Sistema de ranking de fãs com base em nível de interação detectado.

Login e registro de usuários com segurança.

Validação de links/perfis de eSports usando IA para garantir relevância.

Armazenamento e consulta de dados via Supabase.

# 📌 Fluxo do Projeto
Cadastro e Login

Endpoints: /register, /login

Autenticação via Supabase

Onboarding - Rota /dados

Upload de documento (RG ou CNH)

Extração de: Nome, CPF, Endereço, Data de Nascimento via Tesseract

Validação manual pelo usuário

Conexão com Redes Sociais - Rota /redes-sociais

Autorização de acesso à conta do usuário na Twitch

Leitura de:

Contas seguidas

Interações (clipes assistidos, streamers seguidos etc)

Verificação de follow com organizações de eSports como a FURIA

Envio de links de perfis em sites de eSports - também via /redes-sociais

Usuário compartilha links de perfis (ex: HLTV, Liquipedia, Faceit, etc)

verifica se o conteúdo é compatível com o perfil do fã

Classificação de Fã - Rota /redes-sociais

Análise dos dados coletados

Retorno de um “nível de fã” (ex: Novato, Fã Casual, Hardcore, Ultra)

Ranking Geral - Rota /rank

Exibe os fãs mais engajados do sistema

Possibilidade de gamificação

# 🛠️ Como rodar o projeto localmente
Pré-requisitos
Java 17

Maven

Supabase configurado

Tesseract OCR instalado (no caminho /usr/share/tessdata ou equivalente)

Passos
bash
Copiar
Editar
# Clone o repositório
git clone https://github.com/seuusuario/know-your-fan.git
cd know-your-fan

# Configure o application.properties
# Exemplo: src/main/resources/application.properties
properties
Copiar
Editar
spring.datasource.url=jdbc:postgresql://xyz.supabase.co:5432/seubanco
spring.datasource.username=postgres
spring.datasource.password=sua_senha

supabase.api.url=https://xyz.supabase.co
supabase.api.key=...

twitter.api.key=...
twitter.api.secret=...

twitch.client.id=...
twitch.client.secret=...

tesseract.path=/usr/share/tessdata
bash

# Execute o projeto
./mvnw spring-boot:run
Acesse em: http://localhost:8080/home

📁 Endpoints Disponíveis
Endpoint	Descrição
/register	Registro de novos usuários
/login	Login com verificação de credenciais
/dados	Onboarding com upload de documento e confirmação dos dados
/redes-sociais	Autorização e leitura das redes sociais (Twitter, Twitch)
/fan	Classificação e retorno do nível de fã
/rank	Exibição do ranking dos usuários mais engajados
/onboardin Perguntas referente aos interesses
