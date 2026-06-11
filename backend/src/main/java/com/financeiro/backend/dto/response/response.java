package com.financeiro.backend.dto.response;

import java.util.List;

public class response {

    // Resposta de autenticação (login e registro)
    public record AuthResponse(
            String token,
            String nome,
            String email,
            Long id
    ) {}

    // Resposta de uma receita
    public record ReceitaResponse(
            Long id,
            String descricao,
            double valor,
            String data,
            String categoria,
            Long usuarioId
    ) {}

    // Resposta de uma despesa
    public record DespesaResponse(
            Long id,
            String descricao,
            double valor,
            String data,
            String categoria,
            Long usuarioId
    ) {}

    // Resposta genérica de erro/mensagem
    public record MensagemResponse(
            String mensagem
    ) {}

    // Resposta do dashboard com totais
    public record DashboardResponse(
            double totalReceitas,
            double totalDespesas,
            double saldo,
            List<CategoriaValor> despesasPorCategoria
    ) {}

    public record CategoriaValor(
            String categoria,
            double valor
    ) {}
}
