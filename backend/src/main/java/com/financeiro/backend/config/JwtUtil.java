package com.financeiro.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Oque é Claim ?: Claim é uma informação armazenada dentro do token JWT

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Gera o token JWT com o e-mail do usuário como subject
    public String gerarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return criarToken(claims, userDetails.getUsername());
    }

    // Monta e assina o token com tempo de expiração
    private String criarToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    // Extrai o e-mail de dentro do token usado pelo JwtFilter
    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    // Valida se o token pertence ao usuário e não expirou
    public boolean tokenValido(String token, UserDetails userDetails) {
        final String email = extrairEmail(token);
        return (email.equals(userDetails.getUsername())) && !tokenExpirado(token);
    }

    // Verifica se a data de expiração já passou
    private boolean tokenExpirado(String token) {
        return extrairExpiracao(token).before(new Date());
    }

    // Extrai a data de expiração do token
    private Date extrairExpiracao(String token) {
        return extrairClaim(token, Claims::getExpiration);
    }

    // Método genérico que extrai qualquer claim do token
    private <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extrairTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    // Abre e valida o token usando a chave secreta
    private Claims extrairTodosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Converte a string do application.yml em uma SecretKey criptográfica
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}