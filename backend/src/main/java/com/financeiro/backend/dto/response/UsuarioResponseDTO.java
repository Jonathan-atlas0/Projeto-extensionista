package com.financeiro.backend.dto.response;

import com.financeiro.backend.entity.Usuario;

import java.time.LocalDateTime;

// Dados que a API retorna sobre o usuário
// NUNCA inclui a senha — isso é a principal função do DTO de response
public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String role,
        LocalDateTime criadoEm
) {
    // Método estático que converte a entidade Usuario em UsuarioResponseDTO
    // Usado nos Services para não expor a entidade diretamente
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getCriadoEm()
        );
    }
}