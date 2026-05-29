package com.financeiro.backend.service;

import com.financeiro.backend.dto.response.CategoriaResponseDTO;
import com.financeiro.backend.entity.TipoCategoria;
import com.financeiro.backend.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAllByOrderByTipoAscNomeAsc()
                .stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarPorTipo(TipoCategoria tipo) {
        return categoriaRepository.findByTipoOrderByNomeAsc(tipo)
                .stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }
}
