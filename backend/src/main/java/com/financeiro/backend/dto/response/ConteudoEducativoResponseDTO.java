package com.financeiro.backend.dto.response;

import com.financeiro.backend.entity.CategoriaConteudo;
import com.financeiro.backend.entity.ConteudoEducativo;
import com.financeiro.backend.entity.NivelDificuldade;
import java.time.LocalDateTime;

public record ConteudoEducativoResponseDTO(

        Long id,
        String titulo,
        String descricao,
        String conteudo,
        CategoriaConteudo categoriaConteudo,
        NivelDificuldade nivelDificuldade,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        Boolean visivel

) {
    public static  ConteudoEducativoResponseDTO fromEntity (ConteudoEducativo conteudoEducativo){
        return new ConteudoEducativoResponseDTO(
                conteudoEducativo.getId(),
                conteudoEducativo.getTitulo(),
                conteudoEducativo.getDescricao(),
                conteudoEducativo.getConteudo(),
                conteudoEducativo.getCategoriaConteudo(),
                conteudoEducativo.getNivelDificuldade(),
                conteudoEducativo.getCriadoEm(),
                conteudoEducativo.getAtualizadoEm(),
                conteudoEducativo.getVisivel()
        );

    }
}
