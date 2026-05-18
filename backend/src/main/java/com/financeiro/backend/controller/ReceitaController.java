package com.financeiro.backend.controller;

import com.financeiro.backend.dto.request.ReceitaRequestDTO;
import com.financeiro.backend.dto.response.ReceitaResponseDTO;
import com.financeiro.backend.service.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller de Receitas.
 * Todas as rotas exigem token JWT — configurado globalmente no SecurityConfig do Dev 1.
 */
@RestController
@RequestMapping("/api/receitas")
@RequiredArgsConstructor
@Tag(name = "Receitas", description = "Gerenciamento de receitas financeiras do usuário")
@SecurityRequirement(name = "bearerAuth")
public class ReceitaController {

    private final ReceitaService receitaService;

    @PostMapping
    @Operation(summary = "Registrar receita", description = "Cria um novo lançamento de receita")
    public ResponseEntity<ReceitaResponseDTO> criar(@RequestBody @Valid ReceitaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar receitas", description = "Lista todas as receitas do usuário, ordenadas por data")
    public ResponseEntity<List<ReceitaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(receitaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar receita por ID")
    public ResponseEntity<ReceitaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(receitaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar receita")
    public ResponseEntity<ReceitaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ReceitaRequestDTO dto) {
        return ResponseEntity.ok(receitaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar receita")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        receitaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
