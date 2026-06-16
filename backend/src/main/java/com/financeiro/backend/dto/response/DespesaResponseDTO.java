package com.financeiro.backend.dto.response;

import com.financeiro.backend.entity.Despesa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DespesaResponseDTO(
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
    public static DespesaResponseDTO fromEntity(Despesa despesa) {
        return new DespesaResponseDTO(
                despesa.getId(),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData(),
                despesa.getCategoria().getId(),
                despesa.getCategoria().getNome(),
                despesa.getUsuario().getId(),
                despesa.getCriadoEm(),
                despesa.getAtualizadoEm()
        );
    }
}
