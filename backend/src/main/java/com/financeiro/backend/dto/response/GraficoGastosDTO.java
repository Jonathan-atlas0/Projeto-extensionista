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
public class GraficoGastosDTO {

    private String categoria;
    private BigDecimal valor;
    private Double percentual;
}
