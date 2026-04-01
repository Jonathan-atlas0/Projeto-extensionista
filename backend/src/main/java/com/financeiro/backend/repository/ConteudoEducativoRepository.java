package com.financeiro.backend.repository;
import com.financeiro.backend.entity.CategoriaConteudo;
import com.financeiro.backend.entity.ConteudoEducativo;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConteudoEducativoRepository extends JpaRepository<ConteudoEducativo, Long> {
    List<ConteudoEducativo> findByPublicado(Boolean publicado);

    List<ConteudoEducativo> findByCategoriaConteudo(CategoriaConteudo categoriaConteudo);

}
