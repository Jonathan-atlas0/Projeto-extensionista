package com.financeiro.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

// DTO para atualização do perfil do usuário logado
// Apenas o nome pode ser alterado — e-mail e role são protegidos
public record UsuarioPerfilRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome
) {}