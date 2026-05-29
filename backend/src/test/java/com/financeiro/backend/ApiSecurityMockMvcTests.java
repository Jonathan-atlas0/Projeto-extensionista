package com.financeiro.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeiro.backend.dto.request.LoginRequestDTO;
import com.financeiro.backend.dto.request.RegistroRequestDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiSecurityMockMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void devePermitirPreflightCorsDoFrontendLocal() throws Exception {
        mockMvc.perform(options("/api/receitas")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test
    void deveExporSwaggerComEndpointsReais() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/auth/login']").exists())
                .andExpect(jsonPath("$.paths['/api/receitas']").exists())
                .andExpect(jsonPath("$.paths['/api/despesas']").exists())
                .andExpect(jsonPath("$.paths['/api/dashboard']").exists())
                .andExpect(jsonPath("$.paths['/api/relatorios/mensal']").exists())
                .andExpect(jsonPath("$.paths['/api/conteudos']").exists());
    }

    @Test
    void devePermitirListarConteudosPublicosSemToken() throws Exception {
        mockMvc.perform(get("/api/conteudos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveBloquearRotaProtegidaSemToken() throws Exception {
        mockMvc.perform(get("/api/receitas"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.erro").value("Não autorizado"));
    }

    @Test
    void deveBloquearRotaProtegidaComTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/receitas")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.erro").value("Token JWT inválido ou expirado"));
    }

    @Test
    void deveBloquearRotaProtegidaComTokenExpirado() throws Exception {
        mockMvc.perform(get("/api/receitas")
                        .header("Authorization", "Bearer " + criarTokenExpirado("usuario@teste.local")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.erro").value("Token JWT inválido ou expirado"));
    }

    @Test
    void deveRetornarUnauthorizedNoLoginInvalido() throws Exception {
        String email = "usuario-" + UUID.randomUUID() + "@teste.local";

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new RegistroRequestDTO("Usuario Teste", email, "123456")
                        )))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequestDTO(email, "senha-errada")
                        )))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.erro").value("E-mail ou senha inválidos"));
    }

    @Test
    void deveRegistrarLogarERetornarJwt() throws Exception {
        String email = "usuario-" + UUID.randomUUID() + "@teste.local";

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new RegistroRequestDTO("Usuario Teste", email, "123456")
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.senha").doesNotExist());

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequestDTO(email, "123456")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        assertThat(json.get("token").asText()).isNotBlank();
    }

    @Test
    void deveNegarAcessoAdminParaUsuarioComum() throws Exception {
        String token = registrarELogarUsuarioComum();

        mockMvc.perform(get("/api/usuarios/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.erro").value("Acesso negado"));
    }

    @Test
    void deveImpedirUsuarioDeAcessarReceitaEDespesaDeOutroUsuario() throws Exception {
        String tokenUsuarioA = registrarELogarUsuarioComum();
        String tokenUsuarioB = registrarELogarUsuarioComum();
        Long categoriaReceitaId = buscarPrimeiraCategoriaId(tokenUsuarioA, "RECEITA");
        Long categoriaDespesaId = buscarPrimeiraCategoriaId(tokenUsuarioA, "DESPESA");

        Long receitaId = criarReceita(tokenUsuarioA, categoriaReceitaId, "Receita privada", "1000.00", "2026-05-05");
        Long despesaId = criarDespesa(tokenUsuarioA, categoriaDespesaId, "Despesa privada", "150.00", "2026-05-06");

        mockMvc.perform(get("/api/receitas/{id}", receitaId)
                        .header("Authorization", "Bearer " + tokenUsuarioB))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(get("/api/despesas/{id}", despesaId)
                        .header("Authorization", "Bearer " + tokenUsuarioB))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornarBadRequestQuandoCategoriaForIncompativel() throws Exception {
        String token = registrarELogarUsuarioComum();
        Long categoriaReceitaId = buscarPrimeiraCategoriaId(token, "RECEITA");
        Long categoriaDespesaId = buscarPrimeiraCategoriaId(token, "DESPESA");

        mockMvc.perform(post("/api/receitas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Receita invalida",
                                  "valor": 100.00,
                                  "data": "2026-05-10",
                                  "categoriaId": %d
                                }
                                """.formatted(categoriaDespesaId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Categoria informada não é do tipo RECEITA"));

        mockMvc.perform(post("/api/despesas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Despesa invalida",
                                  "valor": 100.00,
                                  "data": "2026-05-10",
                                  "categoriaId": %d
                                }
                                """.formatted(categoriaReceitaId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Categoria informada não é do tipo DESPESA"));
    }

    @Test
    void deveRetornarDashboardMensalComDadosReaisDoUsuario() throws Exception {
        String token = registrarELogarUsuarioComum();
        Long categoriaReceitaId = buscarPrimeiraCategoriaId(token, "RECEITA");
        Long categoriaDespesaId = buscarPrimeiraCategoriaId(token, "DESPESA");

        criarReceita(token, categoriaReceitaId, "Salario", "1500.00", "2026-05-01");
        criarDespesa(token, categoriaDespesaId, "Mercado", "400.00", "2026-05-02");

        mockMvc.perform(get("/api/dashboard")
                        .param("mes", "5")
                        .param("ano", "2026")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mes").value(5))
                .andExpect(jsonPath("$.ano").value(2026))
                .andExpect(jsonPath("$.totalReceitas").value(1500.00))
                .andExpect(jsonPath("$.totalDespesas").value(400.00))
                .andExpect(jsonPath("$.saldo").value(1100.00))
                .andExpect(jsonPath("$.gastosPorCategoria[0].percentual").value(100.0));
    }

    @Test
    void devePermitirAdminCriarConteudoEducativo() throws Exception {
        String tokenAdmin = criarAdminELogar();

        mockMvc.perform(post("/api/conteudos")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Reserva de emergencia",
                                  "descricao": "Como começar uma reserva com pouco dinheiro.",
                                  "conteudo": "Separe uma pequena quantia todo mês antes de gastar com itens não essenciais.",
                                  "categoriaConteudo": "POUPANCA",
                                  "nivelDificuldade": "BASICO",
                                  "visivel": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Reserva de emergencia"))
                .andExpect(jsonPath("$.visivel").value(true));
    }

    @Test
    void deveNegarCriacaoDeConteudoParaUsuarioComum() throws Exception {
        String token = registrarELogarUsuarioComum();

        mockMvc.perform(post("/api/conteudos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Conteudo comum",
                                  "descricao": "Tentativa de criação por usuário comum.",
                                  "conteudo": "Texto de teste.",
                                  "categoriaConteudo": "ORCAMENTO",
                                  "nivelDificuldade": "BASICO",
                                  "visivel": true
                                }
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.erro").value("Acesso negado"));
    }

    @Test
    void deveExecutarCrudCompletoDeReceitasComTokenValido() throws Exception {
        String token = registrarELogarUsuarioComum();
        Long categoriaId = buscarPrimeiraCategoriaId(token, "RECEITA");

        String criacao = mockMvc.perform(post("/api/receitas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Salario de maio",
                                  "valor": 1500.00,
                                  "data": "2026-05-10",
                                  "categoriaId": %d
                                }
                                """.formatted(categoriaId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Salario de maio"))
                .andExpect(jsonPath("$.valor").value(1500.00))
                .andExpect(jsonPath("$.categoriaId").value(categoriaId))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long receitaId = objectMapper.readTree(criacao).get("id").asLong();

        mockMvc.perform(get("/api/receitas")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(receitaId));

        mockMvc.perform(get("/api/receitas/{id}", receitaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Salario de maio"));

        mockMvc.perform(put("/api/receitas/{id}", receitaId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Salario atualizado",
                                  "valor": 1600.00,
                                  "data": "2026-05-11",
                                  "categoriaId": %d
                                }
                                """.formatted(categoriaId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Salario atualizado"))
                .andExpect(jsonPath("$.valor").value(1600.00));

        mockMvc.perform(delete("/api/receitas/{id}", receitaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/receitas/{id}", receitaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveExecutarCrudCompletoDeDespesasComTokenValido() throws Exception {
        String token = registrarELogarUsuarioComum();
        Long categoriaId = buscarPrimeiraCategoriaId(token, "DESPESA");

        String criacao = mockMvc.perform(post("/api/despesas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Mercado",
                                  "valor": 320.50,
                                  "data": "2026-05-12",
                                  "categoriaId": %d
                                }
                                """.formatted(categoriaId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Mercado"))
                .andExpect(jsonPath("$.valor").value(320.50))
                .andExpect(jsonPath("$.categoriaId").value(categoriaId))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long despesaId = objectMapper.readTree(criacao).get("id").asLong();

        mockMvc.perform(get("/api/despesas")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(despesaId));

        mockMvc.perform(get("/api/despesas/{id}", despesaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Mercado"));

        mockMvc.perform(put("/api/despesas/{id}", despesaId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Mercado atualizado",
                                  "valor": 280.75,
                                  "data": "2026-05-13",
                                  "categoriaId": %d
                                }
                                """.formatted(categoriaId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Mercado atualizado"))
                .andExpect(jsonPath("$.valor").value(280.75));

        mockMvc.perform(delete("/api/despesas/{id}", despesaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/despesas/{id}", despesaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    private String registrarELogarUsuarioComum() throws Exception {
        String email = "usuario-" + UUID.randomUUID() + "@teste.local";
        String senha = "123456";

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new RegistroRequestDTO("Usuario Teste", email, senha)
                        )))
                .andExpect(status().isCreated());

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequestDTO(email, senha)
                        )))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    private String criarAdminELogar() throws Exception {
        String email = "admin-" + UUID.randomUUID() + "@teste.local";
        String senha = "123456";

        Usuario admin = Usuario.builder()
                .nome("Admin Teste")
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .role("ROLE_ADMIN")
                .build();
        usuarioRepository.save(admin);

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequestDTO(email, senha)
                        )))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    private Long buscarPrimeiraCategoriaId(String token, String tipo) throws Exception {
        String response = mockMvc.perform(get("/api/categorias/tipo")
                        .param("tipo", tipo)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get(0).get("id").asLong();
    }

    private Long criarReceita(String token, Long categoriaId, String descricao, String valor, String data) throws Exception {
        String response = mockMvc.perform(post("/api/receitas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "%s",
                                  "valor": %s,
                                  "data": "%s",
                                  "categoriaId": %d
                                }
                                """.formatted(descricao, valor, data, categoriaId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private Long criarDespesa(String token, Long categoriaId, String descricao, String valor, String data) throws Exception {
        String response = mockMvc.perform(post("/api/despesas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "%s",
                                  "valor": %s,
                                  "data": "%s",
                                  "categoriaId": %d
                                }
                                """.formatted(descricao, valor, data, categoriaId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private String criarTokenExpirado(String email) {
        long agora = System.currentTimeMillis();

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(agora - 120_000))
                .expiration(new Date(agora - 60_000))
                .signWith(Keys.hmacShaKeyFor("testeFinanceiroSecretKeyComMaisDe32Caracteres".getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
