package com.financeiro.backend.repository;

import com.financeiro.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    //Criação do campo gerado automaticamente pelo nome do findBy
    Optional <Usuario> findByEmail(String email);

    //Verifica se já existe um usuario com esse email no banco
    boolean existsByEmail(String email);
}
