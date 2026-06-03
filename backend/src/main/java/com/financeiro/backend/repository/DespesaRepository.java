package com.financeiro.backend.repository;

import com.financeiro.backend.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByUsuarioIdOrderByDataDescIdDesc(Long usuarioId);

    Optional<Despesa> findByIdAndUsuarioId(Long id, Long usuarioId);

    boolean existsByCategoriaId(Long categoriaId);

    long countByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);

    @Query("""
            select coalesce(sum(d.valor), 0)
            from Despesa d
            where d.usuario.id = :usuarioId
              and d.data between :inicio and :fim
            """)
    BigDecimal somarPorUsuarioEPeriodo(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
            select d.categoria.nome, coalesce(sum(d.valor), 0)
            from Despesa d
            where d.usuario.id = :usuarioId
              and d.data between :inicio and :fim
            group by d.categoria.nome
            order by coalesce(sum(d.valor), 0) desc
            """)
    List<Object[]> somarPorCategoria(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
}
