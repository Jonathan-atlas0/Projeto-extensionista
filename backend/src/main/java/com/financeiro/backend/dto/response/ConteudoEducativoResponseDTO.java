package com.financeiro.backend.dto.response;

import com.financeiro.backend.entity.CategoriaConteudo;
import com.financeiro.backend.entity.NivelDificuldade;
import java.time.LocalDateTime;

public record ConteudoEducativoResponseDTO(

        Long id,
        String titulo,
        String descricao,
        CategoriaConteudo categoriaConteudo,
        NivelDificuldade nivelDificuldade,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        Boolean publicado




) {}
