package com.financeiro.backend;

import com.financeiro.backend.dto.request.ConteudoEducativoRequestDTO;
import com.financeiro.backend.dto.request.DespesaRequestDTO;
import com.financeiro.backend.dto.request.ReceitaRequestDTO;
import com.financeiro.backend.dto.response.DashboardResponseDTO;
import com.financeiro.backend.entity.Categoria;
import com.financeiro.backend.entity.CategoriaConteudo;
import com.financeiro.backend.entity.NivelDificuldade;
import com.financeiro.backend.entity.TipoCategoria;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.repository.CategoriaRepository;
import com.financeiro.backend.repository.UsuarioRepository;
import com.financeiro.backend.service.ConteudoEducativoService;
import com.financeiro.backend.service.DashboardService;
import com.financeiro.backend.service.DespesaService;
import com.financeiro.backend.service.ReceitaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FinanceiroBusinessRulesTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private DespesaService despesaService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ConteudoEducativoService conteudoEducativoService;

    @Test
    void deveImpedirReceitaComCategoriaDeDespesa() {
        Usuario usuario = criarUsuario();
        Categoria categoriaDespesa = primeiraCategoria(TipoCategoria.DESPESA);

        ReceitaRequestDTO dto = new ReceitaRequestDTO(
                "Entrada invalida",
                BigDecimal.valueOf(100),
                LocalDate.of(2026, 5, 10),
                categoriaDespesa.getId()
        );

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> receitaService.criar(usuario.getId(), dto)
        );

        assertThat(exception.getMessage()).contains("RECEITA");
    }

    @Test
    void deveImpedirDespesaComCategoriaDeReceita() {
        Usuario usuario = criarUsuario();
        Categoria categoriaReceita = primeiraCategoria(TipoCategoria.RECEITA);

        DespesaRequestDTO dto = new DespesaRequestDTO(
                "Saida invalida",
                BigDecimal.valueOf(50),
                LocalDate.of(2026, 5, 11),
                categoriaReceita.getId()
        );

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> despesaService.criar(usuario.getId(), dto)
        );

        assertThat(exception.getMessage()).contains("DESPESA");
    }

    @Test
    void deveCalcularDashboardMensalApenasDoUsuarioLogado() {
        Usuario usuario = criarUsuario();
        Usuario outroUsuario = criarUsuario();
        Categoria categoriaReceita = primeiraCategoria(TipoCategoria.RECEITA);
        Categoria categoriaDespesa = primeiraCategoria(TipoCategoria.DESPESA);

        receitaService.criar(usuario.getId(), new ReceitaRequestDTO(
                "Salario",
                BigDecimal.valueOf(1500),
                LocalDate.of(2026, 5, 1),
                categoriaReceita.getId()
        ));
        despesaService.criar(usuario.getId(), new DespesaRequestDTO(
                "Mercado",
                BigDecimal.valueOf(400),
                LocalDate.of(2026, 5, 2),
                categoriaDespesa.getId()
        ));
        despesaService.criar(outroUsuario.getId(), new DespesaRequestDTO(
                "Despesa de outro usuario",
                BigDecimal.valueOf(900),
                LocalDate.of(2026, 5, 2),
                categoriaDespesa.getId()
        ));

        DashboardResponseDTO dashboard = dashboardService.obterDashboard(usuario.getId(), 5, 2026);

        assertThat(dashboard.totalReceitas()).isEqualByComparingTo("1500.00");
        assertThat(dashboard.totalDespesas()).isEqualByComparingTo("400.00");
        assertThat(dashboard.saldo()).isEqualByComparingTo("1100.00");
        assertThat(dashboard.gastosPorCategoria()).hasSize(1);
        assertThat(dashboard.gastosPorCategoria().get(0).percentual()).isEqualTo(100.0);
    }

    @Test
    void deveOcultarConteudoNaoVisivelDaListagemPublica() {
        ConteudoEducativoRequestDTO dto = new ConteudoEducativoRequestDTO(
                "Conteudo interno",
                "Resumo interno",
                "Texto usado apenas para validacao interna.",
                CategoriaConteudo.ORCAMENTO,
                NivelDificuldade.BASICO,
                false
        );

        Long idCriado = conteudoEducativoService.criar(dto).id();

        assertThat(conteudoEducativoService.listarVisiveis())
                .noneMatch(conteudo -> conteudo.id().equals(idCriado));
    }

    private Usuario criarUsuario() {
        return usuarioRepository.save(Usuario.builder()
                .nome("Usuario Teste")
                .email(UUID.randomUUID() + "@teste.local")
                .senha("senha-criptografada")
                .build());
    }

    private Categoria primeiraCategoria(TipoCategoria tipo) {
        return categoriaRepository.findByTipoOrderByNomeAsc(tipo)
                .stream()
                .findFirst()
                .orElseThrow();
    }
}
