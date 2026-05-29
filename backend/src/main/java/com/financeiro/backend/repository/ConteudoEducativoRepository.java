package com.financeiro.backend.repository;

import com.financeiro.backend.entity.ConteudoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConteudoEducativoRepository extends JpaRepository<ConteudoEducativo, Long> {

    List<ConteudoEducativo> findByVisivelTrueOrderByCriadoEmDesc();

    Optional<ConteudoEducativo> findByIdAndVisivelTrue(Long id);
}
