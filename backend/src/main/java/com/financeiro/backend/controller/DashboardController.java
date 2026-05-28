package com.financeiro.backend.controller;

import com.financeiro.backend.dto.response.DashboardDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Visão geral das finanças do usuário")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/dashboard
    // Retorna o painel financeiro completo do usuário autenticado
    // (saldo atual, receitas/despesas do mês, gráfico de gastos e relatório)
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Obter dashboard financeiro", description = "Retorna o resumo completo com saldo atual, receitas e despesas do mês, gráfico de gastos por categoria e relatório do mês atual")
    public ResponseEntity<DashboardDTO> obterDashboard(
            @AuthenticationPrincipal Usuario usuario) {
        DashboardDTO dashboard = dashboardService.obterDashboard(usuario.getId());
        return ResponseEntity.ok(dashboard);
    }
}
