package com.financeiro.backend.controller;

import com.financeiro.backend.dto.request.DespesaRequestDTO;
import com.financeiro.backend.dto.response.DespesaResponseDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.service.DespesaService;
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
@RequestMapping("/api/despesas")
@Tag(name = "Despesas", description = "Controle de despesas do usuário")
@SecurityRequirement(name = "bearerAuth")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar despesa")
    public ResponseEntity<DespesaResponseDTO> criar(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody @Valid DespesaRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaService.criar(usuario.getId(), dto));
    }

    @GetMapping
    @Operation(summary = "Listar minhas despesas")
    public ResponseEntity<List<DespesaResponseDTO>> listarMinhas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(despesaService.listarMinhas(usuario.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar minha despesa por ID")
    public ResponseEntity<DespesaResponseDTO> buscarPorId(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(despesaService.buscarMinhaPorId(usuario.getId(), id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar despesa")
    public ResponseEntity<DespesaResponseDTO> atualizar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id,
            @RequestBody @Valid DespesaRequestDTO dto
    ) {
        return ResponseEntity.ok(despesaService.atualizar(usuario.getId(), id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar despesa")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id
    ) {
        despesaService.deletar(usuario.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
