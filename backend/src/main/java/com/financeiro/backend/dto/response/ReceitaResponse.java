package com.financeiro.backend.dto.response;

import com.financeiro.backend.entity.Receita;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de saída para Receita.
 * Categoria é nullable no response — espelha a opcionalidade da entidade.
 */
public record ReceitaResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        CategoriaResponseDTO categoria
) {
    public static ReceitaResponseDTO from(Receita receita) {
        return new ReceitaResponseDTO(
                receita.getId(),
                receita.getDescricao(),
                receita.getValor(),
                receita.getData(),
                receita.getCategoria() != null
                        ? CategoriaResponseDTO.from(receita.getCategoria())
                        : null
        );
    }
}
