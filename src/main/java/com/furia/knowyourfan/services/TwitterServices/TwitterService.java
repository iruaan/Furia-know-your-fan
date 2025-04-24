package com.furia.knowyourfan.services.TwitterServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterService {

    // As credenciais agora são injetadas do arquivo de propriedades (melhor segurança)
    @Value("${twitter.consumer.key}")
    private String consumerKey;

    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    @Value("${twitter.access.token}")
    private String accessToken;

    @Value("${twitter.access.token.secret}")
    private String accessTokenSecret;

    // Método para autenticar o Twitter
    private Twitter getTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    // Método para verificar se o tweet com a hashtag foi publicado
    public boolean validarTweetComHashtag(String hashtag, String usuarioTwitter) {
        try {
            Twitter twitter = getTwitterInstance();
            Query query = new Query("#" + hashtag + " from:" + usuarioTwitter);
            query.setCount(1);  // Limita a busca a 1 tweet
            QueryResult result = twitter.search(query);

            // Verifica se algum tweet foi encontrado
            if (result.getTweets().isEmpty()) {
                return false;  // Nenhum tweet encontrado
            }

            // Caso haja tweets, retorna verdadeiro
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
            return false;  // Em caso de falha ao consultar a API do Twitter
        }
    }
}
