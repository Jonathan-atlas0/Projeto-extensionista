package com.financeiro.backend.service;

import com.financeiro.backend.dto.request.ConteudoEducativoRequestDTO;
import com.financeiro.backend.dto.response.ConteudoEducativoResponseDTO;
import com.financeiro.backend.entity.ConteudoEducativo;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.ConteudoEducativoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ConteudoEducativoService {
        private final ConteudoEducativoRepository conteudoEducativoRepository;

    public ConteudoEducativoService(ConteudoEducativoRepository conteudoEducativoRepository) {
        this.conteudoEducativoRepository = conteudoEducativoRepository;
    }

    public List<ConteudoEducativoResponseDTO> listarPublicado(){
        return conteudoEducativoRepository.findByPublicado(true)
                .stream()
                .map(ConteudoEducativoResponseDTO::fromEntity)
                .toList();
    }

    public ConteudoEducativoResponseDTO buscarPorId(Long id){
        ConteudoEducativo conteudoEducativo = conteudoEducativoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conteúdo não encontrado com id: " + id
                ));
        return ConteudoEducativoResponseDTO.fromEntity(conteudoEducativo);

    }

    public ConteudoEducativoResponseDTO criar(ConteudoEducativoRequestDTO dto){
        ConteudoEducativo criar = ConteudoEducativo.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .categoriaConteudo(dto.categoriaConteudo())
                .nivelDificuldade(dto.nivelDificuldade())
                .build();
        ConteudoEducativo salvo = conteudoEducativoRepository.save(criar);
        return  ConteudoEducativoResponseDTO.fromEntity(salvo);
    }

    public ConteudoEducativoResponseDTO atualizar(Long id , ConteudoEducativoRequestDTO dto){
        ConteudoEducativo atualizar = conteudoEducativoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conteúdo não encontrado com o id: " + id
                ));
        atualizar.setTitulo(dto.titulo());
        atualizar.setDescricao(dto.descricao());
        atualizar.setCategoriaConteudo(dto.categoriaConteudo());
        atualizar.setNivelDificuldade(dto.nivelDificuldade());
        atualizar.setAtualizadoEm(LocalDateTime.now());

        ConteudoEducativo salvo = conteudoEducativoRepository.save(atualizar);
        return ConteudoEducativoResponseDTO.fromEntity(salvo);


    }

    public void deletarConteudo(Long id){
       if (!conteudoEducativoRepository.existsById(id)){
           throw new ResourceNotFoundException(
                   "Conteudo não encontrado com id: " + id
           );
       }
       conteudoEducativoRepository.deleteById(id);
    }



}
