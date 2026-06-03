package com.financeiro.backend.controller;

import com.financeiro.backend.dto.request.LoginRequestDTO;
import com.financeiro.backend.dto.request.RefreshTokenRequestDTO;
import com.financeiro.backend.dto.request.RegistroRequestDTO;
import com.financeiro.backend.dto.response.LoginResponseDTO;
import com.financeiro.backend.dto.response.UsuarioResponseDTO;
import com.financeiro.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Rotas públicas — não precisam de token JWT
// Por isso não tem @SecurityRequirement aqui
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/auth/registro
    // Cadastra um novo usuário na plataforma
    // @RequestBody → converte o JSON recebido para o DTO
    // @Valid → aciona as validações do DTO (@NotBlank, @Email, @Size)
    // ResponseEntity.status(201) → retorna HTTP 201 CREATED ao invés de 200
    // ─────────────────────────────────────────────────────────────────────────
    @PostMapping("/registro")
    @Operation(summary = "Cadastrar novo usuário")
    public ResponseEntity<UsuarioResponseDTO> registrar(
            @RequestBody @Valid RegistroRequestDTO dto) {
        UsuarioResponseDTO response = authService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/auth/login
    // Autentica o usuário e retorna o token JWT
    // Se as credenciais forem inválidas retorna 401 automaticamente
    // via BadCredentialsException no GlobalExceptionHandler
    // ─────────────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    @Operation(summary = "Realizar login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token de acesso")
    public ResponseEntity<LoginResponseDTO> refresh(
            @RequestBody @Valid RefreshTokenRequestDTO dto) {
        return ResponseEntity.ok(authService.refresh(dto.refreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Revogar refresh token")
    public ResponseEntity<Void> logout(
            @RequestBody @Valid RefreshTokenRequestDTO dto) {
        authService.logout(dto.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
