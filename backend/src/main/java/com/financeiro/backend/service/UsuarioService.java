package com.financeiro.backend.service;

import com.financeiro.backend.dto.request.UsuarioPerfilRequestDTO;
import com.financeiro.backend.dto.response.UsuarioResponseDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ÁREA DO ADMIN
    // ─────────────────────────────────────────────────────────────────────────

    // Lista todos os usuários — apenas admin
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    // Busca qualquer usuário pelo ID — apenas admin
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o id: " + id
                ));
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    // Deleta qualquer usuário pelo ID — apenas admin
    public void deletarPorId(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Usuário não encontrado com o id: " + id
            );
        }
        usuarioRepository.deleteById(id);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ÁREA DO USUÁRIO LOGADO
    // SecurityContextHolder.getContext().getAuthentication().getName()
    // retorna o e-mail do usuário autenticado extraído do token JWT
    // ─────────────────────────────────────────────────────────────────────────

    // Retorna o perfil do próprio usuário logado
    public UsuarioResponseDTO verMeuPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado"
                ));
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    // Atualiza nome e/ou senha do próprio usuário logado
    // Não permite alterar e-mail ou role por segurança
    public UsuarioResponseDTO atualizarMeuPerfil(UsuarioPerfilRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado"
                ));

        // Atualiza apenas os campos permitidos
        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuario.setNome(dto.nome());
        }

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    // Deleta a própria conta do usuário logado
    public void deletarMinhaConta() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado"
                ));
        usuarioRepository.delete(usuario);
    }
}