package com.financeiro.backend.dto.request;

import com.financeiro.backend.entity.CategoriaConteudo;
import com.financeiro.backend.entity.NivelDificuldade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConteudoEducativoRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotBlank(message = "Conteúdo é obrigatório")
        String conteudo,

        @NotNull(message = "Categoria é obrigatória")
        CategoriaConteudo categoriaConteudo,

        @NotNull (message = "Nível de dificuldade é obrigatório")
        NivelDificuldade nivelDificuldade,

        Boolean visivel
        ){}
