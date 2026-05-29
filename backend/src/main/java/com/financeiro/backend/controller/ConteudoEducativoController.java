package com.financeiro.backend.controller;

import com.financeiro.backend.dto.request.ConteudoEducativoRequestDTO;
import com.financeiro.backend.dto.response.ConteudoEducativoResponseDTO;
import com.financeiro.backend.service.ConteudoEducativoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/conteudos")


public class ConteudoEducativoController {

    private final ConteudoEducativoService conteudoEducativoService;

    public ConteudoEducativoController(ConteudoEducativoService conteudoEducativoService) {this.conteudoEducativoService = conteudoEducativoService;}

    @GetMapping()
    @Operation(summary = "[PUBLICADOS] Listar todos as publicações")
    public ResponseEntity<List<ConteudoEducativoResponseDTO>> listarPublicado(){
        return ResponseEntity.ok(conteudoEducativoService.listarVisiveis());
    }

    @GetMapping ("/{id}")
    @Operation(summary = "[PUBLICADOS] Buscar conteúdo por ID")
    public ResponseEntity<ConteudoEducativoResponseDTO>buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(conteudoEducativoService.buscarVisivelPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criação das Publicações")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ConteudoEducativoResponseDTO> criar(
            @RequestBody @Valid ConteudoEducativoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(conteudoEducativoService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualização das Publicações")
    @SecurityRequirement(name = "bearerAuth")
    public  ResponseEntity<ConteudoEducativoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ConteudoEducativoRequestDTO dto){
        return ResponseEntity.ok(conteudoEducativoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta as Publicações")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deletarConteudo(@PathVariable Long id) {
        conteudoEducativoService.deletarConteudo(id);
        return ResponseEntity.noContent().build();
    }



}
