package com.financeiro.backend.controller;

import com.financeiro.backend.dto.request.AlterarSenhaRequestDTO;
import com.financeiro.backend.dto.request.UsuarioPerfilRequestDTO;
import com.financeiro.backend.dto.response.UsuarioResponseDTO;
import com.financeiro.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ROTAS DE ADMIN → /api/usuarios/admin/...
    // Apenas usuários com ROLE_ADMIN conseguem acessar
    // ─────────────────────────────────────────────────────────────────────────

    // GET /api/usuarios/admin
    // Lista todos os usuários cadastrados
    @GetMapping("/admin")
    @Operation(summary = "[ADMIN] Listar todos os usuários")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // GET /api/usuarios/admin/{id}
    // Busca qualquer usuário pelo ID
    @GetMapping("/admin/{id}")
    @Operation(summary = "[ADMIN] Buscar usuário por ID")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // DELETE /api/usuarios/admin/{id}
    // Deleta qualquer usuário pelo ID
    @DeleteMapping("/admin/{id}")
    @Operation(summary = "[ADMIN] Deletar usuário por ID")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        usuarioService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ROTAS DO USUÁRIO LOGADO → /api/usuarios/perfil
    // Qualquer usuário autenticado acessa apenas os próprios dados
    // ─────────────────────────────────────────────────────────────────────────

    // GET /api/usuarios/perfil
    // Retorna os dados do próprio usuário logado
    @GetMapping("/perfil")
    @Operation(summary = "Ver meu perfil")
    public ResponseEntity<UsuarioResponseDTO> verMeuPerfil() {
        return ResponseEntity.ok(usuarioService.verMeuPerfil());
    }

    // PUT /api/usuarios/perfil
    // Atualiza os dados do próprio usuário logado
    @PutMapping("/perfil")
    @Operation(summary = "Atualizar meu perfil")
    public ResponseEntity<UsuarioResponseDTO> atualizarMeuPerfil(
            @RequestBody @Valid UsuarioPerfilRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarMeuPerfil(dto));
    }

    @PutMapping("/perfil/senha")
    @Operation(summary = "Alterar minha senha")
    public ResponseEntity<Void> alterarMinhaSenha(
            @RequestBody @Valid AlterarSenhaRequestDTO dto) {
        usuarioService.alterarMinhaSenha(dto);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/usuarios/perfil
    // Deleta a própria conta do usuário logado
    @DeleteMapping("/perfil")
    @Operation(summary = "Deletar minha conta")
    public ResponseEntity<Void> deletarMinhaConta() {
        usuarioService.deletarMinhaConta();
        return ResponseEntity.noContent().build();
    }
}
