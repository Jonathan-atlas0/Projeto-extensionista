package com.financeiro.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class request {

    // DTO de registro de novo usuário
    public record RegistroRequest(
            @NotBlank(message = "Nome é obrigatório")
            String nome,

            @Email(message = "E-mail inválido")
            @NotBlank(message = "E-mail é obrigatório")
            String email,

            @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
            @NotBlank(message = "Senha é obrigatória")
            String senha
    ) {}

    // DTO de login
    public record LoginRequest(
            @Email(message = "E-mail inválido")
            @NotBlank(message = "E-mail é obrigatório")
            String email,

            @NotBlank(message = "Senha é obrigatória")
            String senha
    ) {}

    // DTO para criar receita
    public record ReceitaRequest(
            @NotBlank(message = "Descrição é obrigatória")
            String descricao,

            double valor,

            @NotBlank(message = "Data é obrigatória")
            String data,

            @NotBlank(message = "Categoria é obrigatória")
            String categoria
    ) {}

    // DTO para criar despesa
    public record DespesaRequest(
            @NotBlank(message = "Descrição é obrigatória")
            String descricao,

            double valor,

            @NotBlank(message = "Data é obrigatória")
            String data,

            @NotBlank(message = "Categoria é obrigatória")
            String categoria
    ) {}
}
