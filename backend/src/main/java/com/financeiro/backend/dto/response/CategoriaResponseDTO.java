package com.financeiro.backend.dto.response;

import com.financeiro.backend.entity.Categoria;
import com.financeiro.backend.entity.TipoCategoria;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        TipoCategoria tipo,
        Boolean padrao
) {
    public static CategoriaResponseDTO fromEntity(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getTipo(),
                categoria.getPadrao()
        );
    }
}
