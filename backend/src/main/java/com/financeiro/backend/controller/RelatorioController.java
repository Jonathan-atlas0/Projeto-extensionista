package com.financeiro.backend.controller;

import com.financeiro.backend.dto.response.GraficoGastosDTO;
import com.financeiro.backend.dto.response.RelatorioMensalDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Geração de relatórios financeiros")
@SecurityRequirement(name = "bearerAuth")
public class RelatorioController {

    private final RelatorioService relatorioService;

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/relatorios/mensal?mes={mes}&ano={ano}
    // Retorna o relatório mensal consolidado do usuário autenticado
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/mensal")
    @Operation(summary = "Gerar relatório mensal", description = "Retorna o resumo financeiro (receitas, despesas, saldo e total de transações) do mês/ano informados")
    public ResponseEntity<RelatorioMensalDTO> gerarRelatorioMensal(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam int mes,
            @RequestParam int ano) {
        RelatorioMensalDTO relatorio = relatorioService.gerarRelatorioMensal(usuario.getId(), mes, ano);
        return ResponseEntity.ok(relatorio);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/relatorios/grafico-gastos?mes={mes}&ano={ano}
    // Retorna os gastos agrupados por categoria para montagem de gráfico
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/grafico-gastos")
    @Operation(summary = "Obter gráfico de gastos por categoria", description = "Retorna os gastos agrupados por categoria com valor e percentual para o mês/ano informados")
    public ResponseEntity<List<GraficoGastosDTO>> obterGraficoGastos(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam int mes,
            @RequestParam int ano) {
        List<GraficoGastosDTO> grafico = relatorioService.obterGraficoGastos(usuario.getId(), mes, ano);
        return ResponseEntity.ok(grafico);
    }
}
