package com.financeiro.backend.service;

import com.financeiro.backend.dto.response.DashboardResponseDTO;
import com.financeiro.backend.dto.response.GraficoGastosDTO;
import com.financeiro.backend.dto.response.RelatorioMensalDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private final RelatorioService relatorioService;

    public DashboardService(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO obterDashboard(Long usuarioId, Integer mes, Integer ano) {
        LocalDate hoje = LocalDate.now();
        int mesReferencia = mes != null ? mes : hoje.getMonthValue();
        int anoReferencia = ano != null ? ano : hoje.getYear();

        RelatorioMensalDTO relatorio = relatorioService.gerarRelatorioMensal(usuarioId, mesReferencia, anoReferencia);
        List<GraficoGastosDTO> gastosPorCategoria = relatorio.gastosPorCategoria();
        String maiorCategoriaGasto = gastosPorCategoria.stream()
                .max(Comparator.comparing(GraficoGastosDTO::total))
                .map(GraficoGastosDTO::categoria)
                .orElse(null);

        return new DashboardResponseDTO(
                mesReferencia,
                anoReferencia,
                relatorio.totalReceitas(),
                relatorio.totalDespesas(),
                relatorio.saldo(),
                maiorCategoriaGasto,
                gastosPorCategoria
        );
    }
}
