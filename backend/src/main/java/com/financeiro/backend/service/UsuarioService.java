package com.financeiro.backend.service;

import com.financeiro.backend.dto.response.UsuarioResponseDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// @Service: registra essa classe como serviço gerenciado pelo Spring
// Camada responsável pelas regras de negócio relacionadas ao usuário
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Lista todos os usuários cadastrados
    // Retorna ResponseDTO para não expor a senha
    // Usado por admins para visualizar todos os usuários
    // ─────────────────────────────────────────────────────────────────────────
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Busca um usuário pelo ID
    // Lança ResourceNotFoundException se não encontrar — capturado pelo GlobalExceptionHandler
    // ─────────────────────────────────────────────────────────────────────────
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o id: " + id
                ));
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Busca um usuário pelo e-mail
    // Usado internamente por outros Services
    // ─────────────────────────────────────────────────────────────────────────
    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o e-mail: " + email
                ));
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Deleta um usuário pelo ID
    // Lança ResourceNotFoundException se o usuário não existir
    // ─────────────────────────────────────────────────────────────────────────
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Usuário não encontrado com o id: " + id
            );
        }
        usuarioRepository.deleteById(id);
    }
}
