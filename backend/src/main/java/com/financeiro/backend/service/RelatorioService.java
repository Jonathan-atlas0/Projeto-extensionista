package com.financeiro.backend.service;

import com.financeiro.backend.dto.response.GraficoGastosDTO;
import com.financeiro.backend.dto.response.RelatorioMensalDTO;
import com.financeiro.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Gera o relatório mensal consolidado para o usuário informado.
     *
     * @param usuarioId ID do usuário autenticado
     * @param mes       mês de referência (1-12)
     * @param ano       ano de referência
     * @return RelatorioMensalDTO com os totais do período
     */
    public RelatorioMensalDTO gerarRelatorioMensal(Long usuarioId, int mes, int ano) {

        return RelatorioMensalDTO.builder()
                .mes(mes)
                .ano(ano)
                .totalReceitas(BigDecimal.ZERO)
                .totalDespesas(BigDecimal.ZERO)
                .saldo(BigDecimal.ZERO)
                .totalTransacoes(0)
                .build();
    }

    /**
     * Retorna os gastos agrupados por categoria para montagem de gráfico.
     *
     * @param usuarioId ID do usuário autenticado
     * @param mes       mês de referência (1-12)
     * @param ano       ano de referência
     * @return lista de GraficoGastosDTO com categoria, valor e percentual
     */
    public List<GraficoGastosDTO> obterGraficoGastos(Long usuarioId, int mes, int ano) {

        return List.of();
    }
}
