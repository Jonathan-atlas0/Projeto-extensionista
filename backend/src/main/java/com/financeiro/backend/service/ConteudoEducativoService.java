package com.financeiro.backend.service;

import com.financeiro.backend.dto.request.ConteudoEducativoRequestDTO;
import com.financeiro.backend.dto.response.ConteudoEducativoResponseDTO;
import com.financeiro.backend.entity.ConteudoEducativo;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.ConteudoEducativoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConteudoEducativoService {

    private final ConteudoEducativoRepository conteudoEducativoRepository;

    public ConteudoEducativoService(ConteudoEducativoRepository conteudoEducativoRepository) {
        this.conteudoEducativoRepository = conteudoEducativoRepository;
    }

    @Transactional(readOnly = true)
    public List<ConteudoEducativoResponseDTO> listarVisiveis() {
        return conteudoEducativoRepository.findByVisivelTrueOrderByCriadoEmDesc()
                .stream()
                .map(ConteudoEducativoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConteudoEducativoResponseDTO buscarVisivelPorId(Long id) {
        ConteudoEducativo conteudoEducativo = conteudoEducativoRepository.findByIdAndVisivelTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conteúdo não encontrado com id: " + id));
        return ConteudoEducativoResponseDTO.fromEntity(conteudoEducativo);
    }

    @Transactional
    public ConteudoEducativoResponseDTO criar(ConteudoEducativoRequestDTO dto) {
        ConteudoEducativo conteudo = ConteudoEducativo.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .conteudo(dto.conteudo())
                .categoriaConteudo(dto.categoriaConteudo())
                .nivelDificuldade(dto.nivelDificuldade())
                .visivel(Boolean.TRUE.equals(dto.visivel()))
                .build();

        return ConteudoEducativoResponseDTO.fromEntity(conteudoEducativoRepository.save(conteudo));
    }

    @Transactional
    public ConteudoEducativoResponseDTO atualizar(Long id, ConteudoEducativoRequestDTO dto) {
        ConteudoEducativo conteudo = buscarPorId(id);

        conteudo.setTitulo(dto.titulo());
        conteudo.setDescricao(dto.descricao());
        conteudo.setConteudo(dto.conteudo());
        conteudo.setCategoriaConteudo(dto.categoriaConteudo());
        conteudo.setNivelDificuldade(dto.nivelDificuldade());
        conteudo.setVisivel(Boolean.TRUE.equals(dto.visivel()));

        return ConteudoEducativoResponseDTO.fromEntity(conteudoEducativoRepository.save(conteudo));
    }

    @Transactional
    public void deletarConteudo(Long id) {
        ConteudoEducativo conteudo = buscarPorId(id);
        conteudoEducativoRepository.delete(conteudo);
    }

    private ConteudoEducativo buscarPorId(Long id) {
        return conteudoEducativoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conteúdo não encontrado com id: " + id));
    }
}
