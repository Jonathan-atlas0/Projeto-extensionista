package com.financeiro.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceitaRequestDTO(
        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.00", inclusive = false, message = "Valor da receita deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "Data é obrigatória")
        @PastOrPresent(message = "Data da receita não pode ser futura")
        LocalDate data,

        @NotNull(message = "Categoria é obrigatória")
        Long categoriaId
) {
}
