package com.financeiro.backend.config;

import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevAdminInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevAdminInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;
    private final String adminName;

    public DevAdminInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${dev-admin.email:}") String adminEmail,
            @Value("${dev-admin.password:}") String adminPassword,
            @Value("${dev-admin.name:Administrador Demo}") String adminName
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminName = adminName;
    }

    @Override
    public void run(String... args) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            LOGGER.info("Admin dev não criado: defina ADMIN_EMAIL e ADMIN_PASSWORD para habilitar.");
            return;
        }

        if (adminPassword.length() < 6) {
            LOGGER.warn("Admin dev não criado: ADMIN_PASSWORD deve ter no mínimo 6 caracteres.");
            return;
        }

        Usuario admin = usuarioRepository.findByEmail(adminEmail)
                .map(usuario -> atualizarAdminExistente(usuario, adminPassword))
                .orElseGet(() -> criarAdmin(adminEmail, adminPassword, adminName));

        usuarioRepository.save(admin);
        LOGGER.info("Admin dev disponível para o e-mail {}", adminEmail);
    }

    private Usuario atualizarAdminExistente(Usuario usuario, String senha) {
        usuario.setRole("ROLE_ADMIN");
        usuario.setSenha(passwordEncoder.encode(senha));
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            usuario.setNome(adminName);
        }
        return usuario;
    }

    private Usuario criarAdmin(String email, String senha, String nome) {
        return Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .role("ROLE_ADMIN")
                .build();
    }
}
