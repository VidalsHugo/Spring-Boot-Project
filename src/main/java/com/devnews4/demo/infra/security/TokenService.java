package com.devnews4.demo.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.devnews4.demo.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    public Set<String> blacklist = new HashSet<>(); // Lista negra para armazenar tokens revogados

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        }catch(JWTCreationException exception){
            throw new RuntimeException("Error while generating Token", exception);
        }
    }
    public String validateToken(String token) {

        // Verifica se o token está na lista negra
        if (isTokenBlacklisted(token)) {
            throw new RuntimeException("Token inválido"); // Lança exceção indicando que o token é inválido
        }

        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException exception){
            return "";
        }
    }
    private boolean isTokenBlacklisted(String token) {
        // Verifica se o token está presente na lista negra
        return blacklist.contains(token);
    }
    private Instant genExpirationDate(){
        // 2 horas para o token expirar depois de criado
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove o prefixo "Bearer " para obter somente o token
        }
        return null;
    }

}
