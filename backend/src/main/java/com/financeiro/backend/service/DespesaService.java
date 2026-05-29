package com.financeiro.backend.service;

import com.financeiro.backend.dto.request.DespesaRequestDTO;
import com.financeiro.backend.dto.response.DespesaResponseDTO;
import com.financeiro.backend.entity.Categoria;
import com.financeiro.backend.entity.Despesa;
import com.financeiro.backend.entity.TipoCategoria;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.CategoriaRepository;
import com.financeiro.backend.repository.DespesaRepository;
import com.financeiro.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public DespesaService(
            DespesaRepository despesaRepository,
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.despesaRepository = despesaRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public DespesaResponseDTO criar(Long usuarioId, DespesaRequestDTO dto) {
        Usuario usuario = buscarUsuario(usuarioId);
        Categoria categoria = buscarCategoriaDoTipo(dto.categoriaId(), TipoCategoria.DESPESA);

        Despesa despesa = Despesa.builder()
                .descricao(dto.descricao())
                .valor(dto.valor())
                .data(dto.data())
                .categoria(categoria)
                .usuario(usuario)
                .build();

        return DespesaResponseDTO.fromEntity(despesaRepository.save(despesa));
    }

    @Transactional(readOnly = true)
    public List<DespesaResponseDTO> listarMinhas(Long usuarioId) {
        return despesaRepository.findByUsuarioIdOrderByDataDescIdDesc(usuarioId)
                .stream()
                .map(DespesaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public DespesaResponseDTO buscarMinhaPorId(Long usuarioId, Long id) {
        return DespesaResponseDTO.fromEntity(buscarDespesaDoUsuario(usuarioId, id));
    }

    @Transactional
    public DespesaResponseDTO atualizar(Long usuarioId, Long id, DespesaRequestDTO dto) {
        Despesa despesa = buscarDespesaDoUsuario(usuarioId, id);
        Categoria categoria = buscarCategoriaDoTipo(dto.categoriaId(), TipoCategoria.DESPESA);

        despesa.setDescricao(dto.descricao());
        despesa.setValor(dto.valor());
        despesa.setData(dto.data());
        despesa.setCategoria(categoria);

        return DespesaResponseDTO.fromEntity(despesaRepository.save(despesa));
    }

    @Transactional
    public void deletar(Long usuarioId, Long id) {
        Despesa despesa = buscarDespesaDoUsuario(usuarioId, id);
        despesaRepository.delete(despesa);
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + usuarioId));
    }

    private Despesa buscarDespesaDoUsuario(Long usuarioId, Long id) {
        return despesaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada com o id: " + id));
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
