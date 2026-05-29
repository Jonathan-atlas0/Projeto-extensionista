package com.financeiro.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "conteudo_educativo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConteudoEducativo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Título é obrigatório")
  @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
  @Column(nullable = false)
  private String titulo;

  @NotBlank(message = "Descrição é obrigatória")
  @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
  @Column(nullable = false, length = 500)
  private String descricao;

  @NotBlank(message = "Conteúdo é obrigatório")
  @Column(nullable = false, columnDefinition = "TEXT")
  private String conteudo;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Categoria é obrigatória")
  @Column(name = "categoria", nullable = false, length = 50)
  private CategoriaConteudo categoriaConteudo;

  @Enumerated(EnumType.STRING)
  @NotNull (message = "Nível de dificuldade é obrigatório")
  @Column(name = "nivel", nullable = false, length = 20)
  private NivelDificuldade nivelDificuldade;

  @Builder.Default
  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm = LocalDateTime.now();

  @Builder.Default
  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm = LocalDateTime.now();

  @Builder.Default
  @Column(nullable = false)
  private Boolean visivel = false;

  @PrePersist
  void prePersist() {
    LocalDateTime agora = LocalDateTime.now();
    criadoEm = agora;
    atualizadoEm = agora;
  }

  @PreUpdate
  void preUpdate() {
    atualizadoEm = LocalDateTime.now();
  }
}
