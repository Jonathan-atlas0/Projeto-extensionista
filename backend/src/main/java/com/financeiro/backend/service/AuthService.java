package com.financeiro.backend.service;

import com.financeiro.backend.config.JwtUtil;
import com.financeiro.backend.dto.request.LoginRequestDTO;
import com.financeiro.backend.dto.request.RegistroRequestDTO;
import com.financeiro.backend.dto.response.LoginResponseDTO;
import com.financeiro.backend.dto.response.UsuarioResponseDTO;
import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.exception.BusinessException;
import com.financeiro.backend.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // REGISTRO — cadastra um novo usuário
    //
    // 1. Verifica se o e-mail já está cadastrado
    // 2. Criptografa a senha com BCrypt antes de salvar
    // 3. Salva o usuário no banco
    // 4. Retorna os dados do usuário (sem a senha)
    // ─────────────────────────────────────────────────────────────────────────
    @Transactional
    public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {

        // Regra de negócio: e-mail único
        // existsByEmail evita buscar o objeto inteiro só para checar duplicidade
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado: " + dto.email());
        }

        // Monta a entidade Usuario usando o Builder do Lombok
        // A senha é criptografada com BCrypt — NUNCA salva em texto puro
        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .build();

        // Salva no banco e converte para DTO de resposta
        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(salvo);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIN — autentica o usuário e gera o token JWT
    //
    // 1. O AuthenticationManager valida e-mail e senha
    // 2. Se inválido lança BadCredentialsException — capturado pelo GlobalExceptionHandler
    // 3. Se válido, gera o token JWT e retorna com os dados do usuário
    // ─────────────────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {

        // O AuthenticationManager aciona o UserDetailsServiceImpl para buscar
        // o usuário no banco e o PasswordEncoder para comparar a senha com o hash
        // Se as credenciais forem inválidas lança BadCredentialsException automaticamente
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        // Recupera o usuário autenticado do contexto
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Gera o token JWT com o e-mail do usuário como subject
        String token = jwtUtil.gerarToken(usuario);
        String refreshToken = refreshTokenService.criar(usuario);

        // Retorna o token + dados básicos do usuário para o frontend
        return new LoginResponseDTO(
                token,
                refreshToken,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

    @Transactional
    public LoginResponseDTO refresh(String refreshTokenAtual) {
        Usuario usuario = refreshTokenService.consumir(refreshTokenAtual);
        String token = jwtUtil.gerarToken(usuario);
        String refreshToken = refreshTokenService.criar(usuario);

        return new LoginResponseDTO(
                token,
                refreshToken,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revogar(refreshToken);
    }
}
