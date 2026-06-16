package com.financeiro.backend.service;

import com.financeiro.backend.dto.request.CategoriaRequestDTO;
import com.financeiro.backend.dto.response.CategoriaResponseDTO;
import com.financeiro.backend.entity.Categoria;
import com.financeiro.backend.entity.TipoCategoria;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.CategoriaRepository;
import com.financeiro.backend.repository.DespesaRepository;
import com.financeiro.backend.repository.ReceitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;

    public CategoriaService(
            CategoriaRepository categoriaRepository,
            ReceitaRepository receitaRepository,
            DespesaRepository despesaRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
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

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        return CategoriaResponseDTO.fromEntity(buscarPorIdInterno(id));
    }

    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        String nome = normalizarNome(dto.nome());
        validarNomeDisponivel(nome, dto.tipo());

        Categoria categoria = Categoria.builder()
                .nome(nome)
                .tipo(dto.tipo())
                .padrao(false)
                .build();

        return CategoriaResponseDTO.fromEntity(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = buscarPorIdInterno(id);

        if (Boolean.TRUE.equals(categoria.getPadrao())) {
            throw new BusinessException("Categorias padrão não podem ser alteradas");
        }

        if (categoria.getTipo() != dto.tipo()) {
            throw new BusinessException("Tipo da categoria não pode ser alterado");
        }

        String nome = normalizarNome(dto.nome());
        if (categoriaRepository.existsByNomeIgnoreCaseAndTipoAndIdNot(nome, dto.tipo(), id)) {
            throw new BusinessException("Categoria já cadastrada para o tipo " + dto.tipo());
        }

        categoria.setNome(nome);
        return CategoriaResponseDTO.fromEntity(categoriaRepository.save(categoria));
    }

    @Transactional
    public void deletar(Long id) {
        Categoria categoria = buscarPorIdInterno(id);

        if (Boolean.TRUE.equals(categoria.getPadrao())) {
            throw new BusinessException("Categorias padrão não podem ser removidas");
        }

        if (receitaRepository.existsByCategoriaId(id) || despesaRepository.existsByCategoriaId(id)) {
            throw new BusinessException("Categoria não pode ser removida porque está em uso");
        }

        categoriaRepository.delete(categoria);
    }

    private Categoria buscarPorIdInterno(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o id: " + id));
    }

    private void validarNomeDisponivel(String nome, TipoCategoria tipo) {
        if (categoriaRepository.existsByNomeIgnoreCaseAndTipo(nome, tipo)) {
            throw new BusinessException("Categoria já cadastrada para o tipo " + tipo);
        }
    }

    private String normalizarNome(String nome) {
        return nome.trim();
    }
}
