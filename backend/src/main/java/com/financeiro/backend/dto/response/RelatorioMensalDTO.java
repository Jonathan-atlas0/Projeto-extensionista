package com.financeiro.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioMensalDTO(
        int mes,
        int ano,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo,
        long totalTransacoes,
        List<GraficoGastosDTO> gastosPorCategoria
) {
}
