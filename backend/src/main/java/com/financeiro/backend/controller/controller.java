package com.financeiro.backend.controller;

import com.financeiro.backend.config.JwtUtil;
import com.financeiro.backend.dto.request.request.*;
import com.financeiro.backend.dto.response.response.*;
import com.financeiro.backend.entity.Despesa;
import com.financeiro.backend.entity.Receita;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.repository.DespesaRepository;
import com.financeiro.backend.repository.ReceitaRepository;
import com.financeiro.backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Classe wrapper — sem anotação própria; os controllers internos são beans independentes.

@org.springframework.stereotype.Component
public class controller {

    // =========================================================
    // AUTH CONTROLLER
    // =========================================================
    @RestController
    @RequestMapping("/api/auth")
    @Tag(name = "Autenticação", description = "Registro e login de usuários")
    public static class AuthController {

        private final UsuarioRepository usuarioRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;

        public AuthController(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager,
                              JwtUtil jwtUtil) {
            this.usuarioRepository = usuarioRepository;
            this.passwordEncoder = passwordEncoder;
            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
        }

        @Operation(summary = "Registrar novo usuário")
        @PostMapping("/registro")
        public ResponseEntity<AuthResponse> registro(@Valid @RequestBody RegistroRequest req) {
            if (usuarioRepository.existsByEmail(req.email())) {
                throw new IllegalArgumentException("E-mail já cadastrado");
            }

            Usuario usuario = Usuario.builder()
                    .nome(req.nome())
                    .email(req.email())
                    .senha(passwordEncoder.encode(req.senha()))
                    .build();

            usuario = usuarioRepository.save(usuario);

            String token = jwtUtil.gerarToken(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(token, usuario.getNome(), usuario.getEmail(), usuario.getId()));
        }

        @Operation(summary = "Login de usuário")
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.senha())
            );

            Usuario usuario = usuarioRepository.findByEmail(req.email())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            String token = jwtUtil.gerarToken(usuario);
            return ResponseEntity.ok(new AuthResponse(token, usuario.getNome(), usuario.getEmail(), usuario.getId()));
        }
    }

    // =========================================================
    // RECEITAS CONTROLLER
    // =========================================================
    @RestController
    @RequestMapping("/api/receitas")
    @SecurityRequirement(name = "bearerAuth")
    @Tag(name = "Receitas", description = "Gerenciamento de receitas do usuário")
    public static class ReceitaController {

        private final ReceitaRepository receitaRepository;
        private final UsuarioRepository usuarioRepository;

        public ReceitaController(ReceitaRepository receitaRepository, UsuarioRepository usuarioRepository) {
            this.receitaRepository = receitaRepository;
            this.usuarioRepository = usuarioRepository;
        }

        @Operation(summary = "Listar todas as receitas do usuário autenticado")
        @GetMapping
        public ResponseEntity<List<ReceitaResponse>> listar(@AuthenticationPrincipal UserDetails userDetails) {
            Usuario usuario = getUsuario(userDetails);
            List<ReceitaResponse> receitas = receitaRepository.findByUsuarioId(usuario.getId())
                    .stream()
                    .map(r -> new ReceitaResponse(r.getId(), r.getDescricao(), r.getValor(),
                            r.getData(), r.getCategoria(), usuario.getId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(receitas);
        }

        @Operation(summary = "Criar nova receita")
        @PostMapping
        public ResponseEntity<ReceitaResponse> criar(@Valid @RequestBody ReceitaRequest req,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
            Usuario usuario = getUsuario(userDetails);
            Receita receita = Receita.builder()
                    .descricao(req.descricao())
                    .valor(req.valor())
                    .data(req.data())
                    .categoria(req.categoria())
                    .usuario(usuario)
                    .build();
            receita = receitaRepository.save(receita);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ReceitaResponse(receita.getId(), receita.getDescricao(), receita.getValor(),
                            receita.getData(), receita.getCategoria(), usuario.getId()));
        }

        @Operation(summary = "Deletar receita por ID")
        @DeleteMapping("/{id}")
        public ResponseEntity<MensagemResponse> deletar(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
            Usuario usuario = getUsuario(userDetails);
            Receita receita = receitaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada"));
            if (!receita.getUsuario().getId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MensagemResponse("Acesso negado"));
            }
            receitaRepository.delete(receita);
            return ResponseEntity.ok(new MensagemResponse("Receita removida com sucesso"));
        }

        private Usuario getUsuario(UserDetails userDetails) {
            return usuarioRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        }
    }

    // =========================================================
    // DESPESAS CONTROLLER
    // =========================================================
    @RestController
    @RequestMapping("/api/despesas")
    @SecurityRequirement(name = "bearerAuth")
    @Tag(name = "Despesas", description = "Gerenciamento de despesas do usuário")
    public static class DespesaController {

        private final DespesaRepository despesaRepository;
        private final UsuarioRepository usuarioRepository;

        public DespesaController(DespesaRepository despesaRepository, UsuarioRepository usuarioRepository) {
            this.despesaRepository = despesaRepository;
            this.usuarioRepository = usuarioRepository;
        }

        @Operation(summary = "Listar todas as despesas do usuário autenticado")
        @GetMapping
        public ResponseEntity<List<DespesaResponse>> listar(@AuthenticationPrincipal UserDetails userDetails) {
            Usuario usuario = getUsuario(userDetails);
            List<DespesaResponse> despesas = despesaRepository.findByUsuarioId(usuario.getId())
                    .stream()
                    .map(d -> new DespesaResponse(d.getId(), d.getDescricao(), d.getValor(),
                            d.getData(), d.getCategoria(), usuario.getId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(despesas);
        }

        @Operation(summary = "Criar nova despesa")
        @PostMapping
        public ResponseEntity<DespesaResponse> criar(@Valid @RequestBody DespesaRequest req,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
            Usuario usuario = getUsuario(userDetails);
            Despesa despesa = Despesa.builder()
                    .descricao(req.descricao())
                    .valor(req.valor())
                    .data(req.data())
                    .categoria(req.categoria())
                    .usuario(usuario)
                    .build();
            despesa = despesaRepository.save(despesa);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new DespesaResponse(despesa.getId(), despesa.getDescricao(), despesa.getValor(),
                            despesa.getData(), despesa.getCategoria(), usuario.getId()));
        }

        @Operation(summary = "Deletar despesa por ID")
        @DeleteMapping("/{id}")
        public ResponseEntity<MensagemResponse> deletar(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
            Usuario usuario = getUsuario(userDetails);
            Despesa despesa = despesaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Despesa não encontrada"));
            if (!despesa.getUsuario().getId().equals(usuario.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MensagemResponse("Acesso negado"));
            }
            despesaRepository.delete(despesa);
            return ResponseEntity.ok(new MensagemResponse("Despesa removida com sucesso"));
        }

        private Usuario getUsuario(UserDetails userDetails) {
            return usuarioRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        }
    }

    // =========================================================
    // DASHBOARD CONTROLLER
    // =========================================================
    @RestController
    @RequestMapping("/api/dashboard")
    @SecurityRequirement(name = "bearerAuth")
    @Tag(name = "Dashboard", description = "Resumo financeiro do usuário")
    public static class DashboardController {

        private final ReceitaRepository receitaRepository;
        private final DespesaRepository despesaRepository;
        private final UsuarioRepository usuarioRepository;

        public DashboardController(ReceitaRepository receitaRepository,
                                   DespesaRepository despesaRepository,
                                   UsuarioRepository usuarioRepository) {
            this.receitaRepository = receitaRepository;
            this.despesaRepository = despesaRepository;
            this.usuarioRepository = usuarioRepository;
        }

        @Operation(summary = "Obter resumo financeiro do usuário")
        @GetMapping
        public ResponseEntity<DashboardResponse> resumo(@AuthenticationPrincipal UserDetails userDetails) {
            Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            List<Receita> receitas = receitaRepository.findByUsuarioId(usuario.getId());
            List<Despesa> despesas = despesaRepository.findByUsuarioId(usuario.getId());

            double totalReceitas = receitas.stream().mapToDouble(Receita::getValor).sum();
            double totalDespesas = despesas.stream().mapToDouble(Despesa::getValor).sum();

            Map<String, Double> porCategoria = despesas.stream()
                    .collect(Collectors.groupingBy(Despesa::getCategoria,
                            Collectors.summingDouble(Despesa::getValor)));

            List<CategoriaValor> categorias = porCategoria.entrySet().stream()
                    .map(e -> new CategoriaValor(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new DashboardResponse(
                    totalReceitas, totalDespesas, totalReceitas - totalDespesas, categorias));
        }
    }
}
