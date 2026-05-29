package com.financeiro.backend.repository;

import com.financeiro.backend.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List<Receita> findByUsuarioIdOrderByDataDescIdDesc(Long usuarioId);

    Optional<Receita> findByIdAndUsuarioId(Long id, Long usuarioId);

    long countByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);

    @Query("""
            select coalesce(sum(r.valor), 0)
            from Receita r
            where r.usuario.id = :usuarioId
              and r.data between :inicio and :fim
            """)
    BigDecimal somarPorUsuarioEPeriodo(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
}
