package com.financeiro.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para criação e atualização de Receita.
 * categoriaId é opcional — uma receita pode existir sem categoria.
 */
public record ReceitaRequestDTO(

        @NotBlank(message = "A descrição é obrigatória.")
        @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres.")
        String descricao,

        @NotNull(message = "O valor é obrigatório.")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "A data é obrigatória.")
        LocalDate data,

        Long categoriaId // nullable — categoria é opcional

) {}
