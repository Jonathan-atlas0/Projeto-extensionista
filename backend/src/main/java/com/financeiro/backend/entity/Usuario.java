package com.financeiro.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

  //Set do Id do usuario
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //Set da estrutura do nome do usuario
  @NotBlank(message = "Nome é Obrigatório")
  @Column(nullable = false, length = 100)
  private String nome;

  //Set da estrutra de anexo de email
  @Email(message = "E-mail inválido")
  @NotBlank(message = "E-mail é obrigatório")
  @Column(nullable = false, unique = true, length = 150)
  private String email;

  //Set da estrutura e validação de senha
  @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
  @NotBlank(message = "Senha é obrigatória")
  @Column(nullable = false, length = 255)
  private  String senha;

  //Set do construtor padrão de usuario
  @Builder.Default
  @Column(nullable = false, length = 20)
  private String role = "ROLE_USER";

  // Set do horario do cadastro
  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

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

  // Metodo que retorna e gerenciar as permições e autenticações dos usuarios
  @Override
  public Collection<? extends GrantedAuthority > getAuthorities(){
    return List.of(new SimpleGrantedAuthority(role));
  }

  //metodo que pega a senha do usuario
  @Override
  public String getPassword(){
    return senha;
  }

  //metodo que pega o username do usuario, que no caso é o email registrado
  @Override
  public String getUsername(){
    return email;
  }

  // Metodo de verificação  dos status da conta, todos vão retornar true porque n vamos implentar eles, mas o spring nescessita de metodos desse tipo
  @Override
  public boolean isAccountNonExpired(){
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired(){
    return true;
  }

  //Deixaremos true pois caso esse metodo seja False o Spring ira bloquear o login do usuario
  @Override
  public boolean isEnabled(){
    return true;
  }

  }
