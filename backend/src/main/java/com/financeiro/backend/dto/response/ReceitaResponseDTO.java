package com.financeiro.backend.dto.response;

import com.financeiro.backend.entity.Receita;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReceitaResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        Long categoriaId,
        String categoriaNome,
        Long usuarioId,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static ReceitaResponseDTO fromEntity(Receita receita) {
        return new ReceitaResponseDTO(
                receita.getId(),
                receita.getDescricao(),
                receita.getValor(),
                receita.getData(),
                receita.getCategoria().getId(),
                receita.getCategoria().getNome(),
                receita.getUsuario().getId(),
                receita.getCriadoEm(),
                receita.getAtualizadoEm()
        );
    }
}
