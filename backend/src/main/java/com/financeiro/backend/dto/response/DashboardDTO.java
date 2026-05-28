package com.financeiro.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private BigDecimal saldoAtual;
    private BigDecimal receitasMes;
    private BigDecimal despesasMes;
    private List<GraficoGastosDTO> graficoGastos;
    private RelatorioMensalDTO relatorioMesAtual;
}
