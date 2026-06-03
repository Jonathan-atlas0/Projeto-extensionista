package com.financeiro.backend.service;

import com.financeiro.backend.entity.RefreshToken;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Long refreshExpirationMillis;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.refresh-expiration}") Long refreshExpirationMillis
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    @Transactional
    public String criar(Usuario usuario) {
        String token = gerarTokenUnico();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .usuario(usuario)
                .expiraEm(LocalDateTime.now().plusNanos(refreshExpirationMillis * 1_000_000))
                .revogado(false)
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    @Transactional
    public Usuario consumir(String token) {
        RefreshToken refreshToken = buscar(token);
        validarAtivo(refreshToken);
        revogar(refreshToken);
        return refreshToken.getUsuario();
    }

    @Transactional
    public void revogar(String token) {
        RefreshToken refreshToken = buscar(token);
        validarAtivo(refreshToken);
        revogar(refreshToken);
    }

    private RefreshToken buscar(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Refresh token inválido ou expirado"));
    }

    private void validarAtivo(RefreshToken refreshToken) {
        if (Boolean.TRUE.equals(refreshToken.getRevogado()) || refreshToken.getExpiraEm().isBefore(LocalDateTime.now())) {
            if (!Boolean.TRUE.equals(refreshToken.getRevogado())) {
                revogar(refreshToken);
            }
            throw new BusinessException("Refresh token inválido ou expirado");
        }
    }

    private void revogar(RefreshToken refreshToken) {
        refreshToken.setRevogado(true);
        refreshToken.setRevogadoEm(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }

    private String gerarTokenUnico() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (refreshTokenRepository.existsByToken(token));
        return token;
    }
}
