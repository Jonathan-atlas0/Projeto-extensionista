package com.financeiro.backend.repository;

import com.financeiro.backend.entity.Categoria;
import com.financeiro.backend.entity.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByTipoOrderByNomeAsc(TipoCategoria tipo);

    List<Categoria> findAllByOrderByTipoAscNomeAsc();

    boolean existsByNomeIgnoreCaseAndTipo(String nome, TipoCategoria tipo);

    boolean existsByNomeIgnoreCaseAndTipoAndIdNot(String nome, TipoCategoria tipo, Long id);
}
