package com.financeiro.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados que o frontend envia para cadastrar um novo usuário
public record RegistroRequestDTO(

        // @NotBlank: não pode ser nulo nem vazio
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        // @Email: valida o formato do e-mail
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        // @Size: mínimo de 6 caracteres
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @NotBlank(message = "Senha é obrigatória")
        String senha
) {}