package com.financeiro.backend.controller;

import com.financeiro.backend.dto.response.CategoriaResponseDTO;
import com.financeiro.backend.entity.TipoCategoria;
import com.financeiro.backend.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Categorias de receitas e despesas")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/tipo")
    @Operation(summary = "Listar categorias por tipo")
    public ResponseEntity<List<CategoriaResponseDTO>> listarPorTipo(@RequestParam TipoCategoria tipo) {
        return ResponseEntity.ok(categoriaService.listarPorTipo(tipo));
    }
}
