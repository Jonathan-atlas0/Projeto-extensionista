package com.financeiro.backend.controller;

import com.financeiro.backend.dto.request.ReceitaRequestDTO;
import com.financeiro.backend.dto.response.ReceitaResponseDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.service.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/receitas")
@Tag(name = "Receitas", description = "Controle de receitas do usuário")
@SecurityRequirement(name = "bearerAuth")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar receita")
    public ResponseEntity<ReceitaResponseDTO> criar(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody @Valid ReceitaRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.criar(usuario.getId(), dto));
    }

    @GetMapping
    @Operation(summary = "Listar minhas receitas")
    public ResponseEntity<List<ReceitaResponseDTO>> listarMinhas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(receitaService.listarMinhas(usuario.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar minha receita por ID")
    public ResponseEntity<ReceitaResponseDTO> buscarPorId(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(receitaService.buscarMinhaPorId(usuario.getId(), id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar receita")
    public ResponseEntity<ReceitaResponseDTO> atualizar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id,
            @RequestBody @Valid ReceitaRequestDTO dto
    ) {
        return ResponseEntity.ok(receitaService.atualizar(usuario.getId(), id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar receita")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id
    ) {
        receitaService.deletar(usuario.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
