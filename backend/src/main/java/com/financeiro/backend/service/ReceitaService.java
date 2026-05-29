package com.financeiro.backend.service;

import com.financeiro.backend.dto.request.ReceitaRequestDTO;
import com.financeiro.backend.dto.response.ReceitaResponseDTO;
import com.financeiro.backend.entity.Categoria;
import com.financeiro.backend.entity.Receita;
import com.financeiro.backend.entity.TipoCategoria;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.CategoriaRepository;
import com.financeiro.backend.repository.ReceitaRepository;
import com.financeiro.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReceitaService(
            ReceitaRepository receitaRepository,
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.receitaRepository = receitaRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ReceitaResponseDTO criar(Long usuarioId, ReceitaRequestDTO dto) {
        Usuario usuario = buscarUsuario(usuarioId);
        Categoria categoria = buscarCategoriaDoTipo(dto.categoriaId(), TipoCategoria.RECEITA);

        Receita receita = Receita.builder()
                .descricao(dto.descricao())
                .valor(dto.valor())
                .data(dto.data())
                .categoria(categoria)
                .usuario(usuario)
                .build();

        return ReceitaResponseDTO.fromEntity(receitaRepository.save(receita));
    }

    @Transactional(readOnly = true)
    public List<ReceitaResponseDTO> listarMinhas(Long usuarioId) {
        return receitaRepository.findByUsuarioIdOrderByDataDescIdDesc(usuarioId)
                .stream()
                .map(ReceitaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReceitaResponseDTO buscarMinhaPorId(Long usuarioId, Long id) {
        return ReceitaResponseDTO.fromEntity(buscarReceitaDoUsuario(usuarioId, id));
    }

    @Transactional
    public ReceitaResponseDTO atualizar(Long usuarioId, Long id, ReceitaRequestDTO dto) {
        Receita receita = buscarReceitaDoUsuario(usuarioId, id);
        Categoria categoria = buscarCategoriaDoTipo(dto.categoriaId(), TipoCategoria.RECEITA);

        receita.setDescricao(dto.descricao());
        receita.setValor(dto.valor());
        receita.setData(dto.data());
        receita.setCategoria(categoria);

        return ReceitaResponseDTO.fromEntity(receitaRepository.save(receita));
    }

    @Transactional
    public void deletar(Long usuarioId, Long id) {
        Receita receita = buscarReceitaDoUsuario(usuarioId, id);
        receitaRepository.delete(receita);
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + usuarioId));
    }

    private Receita buscarReceitaDoUsuario(Long usuarioId, Long id) {
        return receitaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada com o id: " + id));
    }

    private Categoria buscarCategoriaDoTipo(Long categoriaId, TipoCategoria tipoEsperado) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o id: " + categoriaId));

        if (categoria.getTipo() != tipoEsperado) {
            throw new BusinessException("Categoria informada não é do tipo " + tipoEsperado);
        }

        return categoria;
    }
}
