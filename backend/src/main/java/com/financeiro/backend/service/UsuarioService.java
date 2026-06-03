package com.financeiro.backend.service;

import com.financeiro.backend.dto.request.AlterarSenhaRequestDTO;
import com.financeiro.backend.dto.request.UsuarioPerfilRequestDTO;
import com.financeiro.backend.dto.response.UsuarioResponseDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.exception.ResourceNotFoundException;
import com.financeiro.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ÁREA DO ADMIN
    // ─────────────────────────────────────────────────────────────────────────

    // Lista todos os usuários — apenas admin
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    // Busca qualquer usuário pelo ID — apenas admin
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o id: " + id
                ));
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    // Deleta qualquer usuário pelo ID — apenas admin
    @Transactional
    public void deletarPorId(Long id) {
        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (usuarioLogado.getId().equals(id)) {
            throw new BusinessException("Admin não pode deletar a própria conta pelo endpoint admin");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }

        usuarioRepository.deleteById(id);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ÁREA DO USUÁRIO LOGADO
    // SecurityContextHolder.getContext().getAuthentication().getName()
    // retorna o e-mail do usuário autenticado extraído do token JWT
    // ─────────────────────────────────────────────────────────────────────────

    // Retorna o perfil do próprio usuário logado
    @Transactional(readOnly = true)
    public UsuarioResponseDTO verMeuPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado"
                ));
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    // Atualiza apenas o nome do próprio usuário logado
    // Não permite alterar e-mail ou role por segurança
    @Transactional
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

    @Transactional
    public void alterarMinhaSenha(AlterarSenhaRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado"
                ));

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new BusinessException("Senha atual inválida");
        }

        if (!dto.novaSenha().equals(dto.confirmacaoNovaSenha())) {
            throw new BusinessException("Nova senha e confirmação não conferem");
        }

        if (passwordEncoder.matches(dto.novaSenha(), usuario.getSenha())) {
            throw new BusinessException("Nova senha deve ser diferente da senha atual");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }

    // Deleta a própria conta do usuário logado
    @Transactional
    public void deletarMinhaConta() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado"
                ));
        usuarioRepository.delete(usuario);
    }
}
