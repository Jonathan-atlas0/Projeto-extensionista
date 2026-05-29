package com.financeiro.backend.dto.response;

import java.math.BigDecimal;

public record GraficoGastosDTO(
        String categoria,
        BigDecimal total,
        Double percentual
) {
}
