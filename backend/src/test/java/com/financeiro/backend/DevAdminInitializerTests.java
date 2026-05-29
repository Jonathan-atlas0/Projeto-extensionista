package com.financeiro.backend;

import com.financeiro.backend.entity.Usuario;
import com.financeiro.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "ADMIN_EMAIL=admin-dev@teste.local",
        "ADMIN_PASSWORD=123456",
        "ADMIN_NAME=Admin Dev Teste"
})
@ActiveProfiles({"test", "dev"})
class DevAdminInitializerTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void deveCriarAdminDevQuandoVariaveisForemInformadas() {
        Usuario admin = usuarioRepository.findByEmail("admin-dev@teste.local")
                .orElseThrow();

        assertThat(admin.getNome()).isEqualTo("Admin Dev Teste");
        assertThat(admin.getRole()).isEqualTo("ROLE_ADMIN");
        assertThat(passwordEncoder.matches("123456", admin.getSenha())).isTrue();
    }
}
