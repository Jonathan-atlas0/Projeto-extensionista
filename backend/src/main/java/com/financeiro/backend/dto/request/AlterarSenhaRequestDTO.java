package com.financeiro.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaRequestDTO(
        @NotBlank(message = "Senha atual é obrigatória")
        String senhaAtual,

        @Size(min = 6, message = "Nova senha deve ter no mínimo 6 caracteres")
        @NotBlank(message = "Nova senha é obrigatória")
        String novaSenha,

        @NotBlank(message = "Confirmação da nova senha é obrigatória")
        String confirmacaoNovaSenha
) {
}
