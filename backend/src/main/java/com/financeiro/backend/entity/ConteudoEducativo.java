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
@Table(name = "conteudos_educativos")
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
  @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
  @Column(nullable = false, columnDefinition = "TEXT")
  private String descricao;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Categoria é obrigatória")
  @Column(nullable = false)
  public CategoriaConteudo categoriaConteudo;

  @Enumerated(EnumType.STRING)
  @NotNull (message = "Nível de dificuldade é obrigatório")
  @Column(nullable = false)
  private NivelDificuldade nivelDificuldade;

  @Builder.Default
  @Column(nullable = false, updatable = false)
  private LocalDateTime criadoEm = LocalDateTime.now();

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime atualizadoEm = LocalDateTime.now();

  @Builder.Default
  @Column(nullable = false)
  public Boolean publicado = false;
}
