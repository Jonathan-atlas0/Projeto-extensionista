package com.financeiro.backend.service;

import com.financeiro.backend.dto.response.DashboardDTO;
import com.financeiro.backend.dto.response.RelatorioMensalDTO;
import com.financeiro.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Retorna o painel financeiro completo do usuário autenticado.
     *
     * @param usuarioId ID do usuário autenticado
     * @return DashboardDTO com saldo, receitas/despesas do mês, gráfico e relatório
     */
    public DashboardDTO obterDashboard(Long usuarioId) {

        return DashboardDTO.builder()
                .saldoAtual(BigDecimal.ZERO)
                .receitasMes(BigDecimal.ZERO)
                .despesasMes(BigDecimal.ZERO)
                .graficoGastos(List.of())
                .relatorioMesAtual(gerarRelatorioAtual(usuarioId))
                .build();
    }

    /**
     * Gera o relatório do mês/ano atuais como apoio ao dashboard.
     *
     * @param usuarioId ID do usuário autenticado
     * @return RelatorioMensalDTO com mês e ano correntes e valores zerados
     */
    private RelatorioMensalDTO gerarRelatorioAtual(Long usuarioId) {
        LocalDate hoje = LocalDate.now();


        return RelatorioMensalDTO.builder()
                .mes(hoje.getMonthValue())
                .ano(hoje.getYear())
                .totalReceitas(BigDecimal.ZERO)
                .totalDespesas(BigDecimal.ZERO)
                .saldo(BigDecimal.ZERO)
                .totalTransacoes(0)
                .build();
    }
}
