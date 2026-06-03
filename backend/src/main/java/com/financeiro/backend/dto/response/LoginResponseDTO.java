package com.financeiro.backend.dto.response;

// Dados que a API retorna após um login bem-sucedido
// Retorna o token JWT e os dados básicos do usuário
public record LoginResponseDTO(
        String token,
        String tipo,
        String refreshToken,
        String nome,
        String email,
        String role
) {
    // Construtor auxiliar que define "Bearer" como tipo padrão do token
    public LoginResponseDTO(String token, String refreshToken, String nome, String email, String role) {
        this(token, "Bearer", refreshToken, nome, email, role);
    }
}
