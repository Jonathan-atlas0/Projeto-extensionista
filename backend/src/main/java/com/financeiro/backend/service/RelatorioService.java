package com.financeiro.backend.service;

import com.financeiro.backend.dto.response.GraficoGastosDTO;
import com.financeiro.backend.dto.response.RelatorioMensalDTO;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.repository.DespesaRepository;
import com.financeiro.backend.repository.ReceitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioService {

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;

    public RelatorioService(ReceitaRepository receitaRepository, DespesaRepository despesaRepository) {
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
    }

    @Transactional(readOnly = true)
    public RelatorioMensalDTO gerarRelatorioMensal(Long usuarioId, int mes, int ano) {
        PeriodoMensal periodo = criarPeriodo(mes, ano);
        BigDecimal totalReceitas = receitaRepository.somarPorUsuarioEPeriodo(usuarioId, periodo.inicio(), periodo.fim());
        BigDecimal totalDespesas = despesaRepository.somarPorUsuarioEPeriodo(usuarioId, periodo.inicio(), periodo.fim());
        long totalTransacoes = receitaRepository.countByUsuarioIdAndDataBetween(usuarioId, periodo.inicio(), periodo.fim())
                + despesaRepository.countByUsuarioIdAndDataBetween(usuarioId, periodo.inicio(), periodo.fim());

        return new RelatorioMensalDTO(
                mes,
                ano,
                totalReceitas,
                totalDespesas,
                totalReceitas.subtract(totalDespesas),
                totalTransacoes,
                obterGraficoGastos(usuarioId, mes, ano)
        );
    }

    @Transactional(readOnly = true)
    public RelatorioMensalDTO gerarRelatorioAcumulado(Long usuarioId, int mes, int ano) {
        PeriodoMensal periodo = criarPeriodo(mes, ano);
        BigDecimal totalReceitas = receitaRepository.somarPorUsuarioAteData(usuarioId, periodo.fim());
        BigDecimal totalDespesas = despesaRepository.somarPorUsuarioAteData(usuarioId, periodo.fim());
        long totalTransacoes = receitaRepository.countByUsuarioIdAndDataLessThanEqual(usuarioId, periodo.fim())
                + despesaRepository.countByUsuarioIdAndDataLessThanEqual(usuarioId, periodo.fim());

        return new RelatorioMensalDTO(
                mes,
                ano,
                totalReceitas,
                totalDespesas,
                totalReceitas.subtract(totalDespesas),
                totalTransacoes,
                obterGraficoGastosAcumulado(usuarioId, mes, ano)
        );
    }

    @Transactional(readOnly = true)
    public List<GraficoGastosDTO> obterGraficoGastos(Long usuarioId, int mes, int ano) {
        PeriodoMensal periodo = criarPeriodo(mes, ano);
        BigDecimal totalDespesas = despesaRepository.somarPorUsuarioEPeriodo(usuarioId, periodo.inicio(), periodo.fim());

        return despesaRepository.somarPorCategoria(usuarioId, periodo.inicio(), periodo.fim())
                .stream()
                .map(resultado -> criarGraficoGasto(resultado, totalDespesas))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GraficoGastosDTO> obterGraficoGastosAcumulado(Long usuarioId, int mes, int ano) {
        PeriodoMensal periodo = criarPeriodo(mes, ano);
        BigDecimal totalDespesas = despesaRepository.somarPorUsuarioAteData(usuarioId, periodo.fim());

        return despesaRepository.somarPorCategoriaAteData(usuarioId, periodo.fim())
                .stream()
                .map(resultado -> criarGraficoGasto(resultado, totalDespesas))
                .toList();
    }

    private GraficoGastosDTO criarGraficoGasto(Object[] resultado, BigDecimal totalDespesas) {
        String categoria = (String) resultado[0];
        BigDecimal totalCategoria = (BigDecimal) resultado[1];
        double percentual = BigDecimal.ZERO.compareTo(totalDespesas) == 0
                ? 0.0
                : totalCategoria
                        .multiply(BigDecimal.valueOf(100))
                        .divide(totalDespesas, 2, RoundingMode.HALF_UP)
                        .doubleValue();

        return new GraficoGastosDTO(categoria, totalCategoria, percentual);
    }

    private PeriodoMensal criarPeriodo(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new BusinessException("Mês deve estar entre 1 e 12");
        }
        if (ano < 1900) {
            throw new BusinessException("Ano informado é inválido");
        }

        LocalDate inicio = LocalDate.of(ano, mes, 1);
        return new PeriodoMensal(inicio, inicio.withDayOfMonth(inicio.lengthOfMonth()));
    }

    private record PeriodoMensal(LocalDate inicio, LocalDate fim) {
    }
}
