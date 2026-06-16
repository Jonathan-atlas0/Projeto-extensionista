package com.financeiro.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponseDTO(
        int mes,
        int ano,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo,
        String maiorCategoriaGasto,
        List<GraficoGastosDTO> gastosPorCategoria
) {
}
