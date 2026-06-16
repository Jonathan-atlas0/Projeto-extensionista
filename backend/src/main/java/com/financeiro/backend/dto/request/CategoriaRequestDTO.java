package com.financeiro.backend.dto.request;

import com.financeiro.backend.entity.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
        @NotBlank(message = "Nome da categoria é obrigatório")
        @Size(max = 50, message = "Nome da categoria deve ter no máximo 50 caracteres")
        String nome,

        @NotNull(message = "Tipo da categoria é obrigatório")
        TipoCategoria tipo
) {
}
