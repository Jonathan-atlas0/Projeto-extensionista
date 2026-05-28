package com.financeiro.backend.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMensalDTO {

    private int mes;
    private int ano;
    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal saldo;
    private int totalTransacoes;
}
