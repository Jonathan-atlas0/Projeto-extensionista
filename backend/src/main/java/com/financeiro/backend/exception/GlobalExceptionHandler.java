package com.financeiro.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


// retorna uma resposta JSON padronizada ao invés de uma página de erro
@RestControllerAdvice
public class GlobalExceptionHandler {


    // Retorna 404 NOT FOUND quando um recurso não existe no banco
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(criarResposta(ex.getMessage(), HttpStatus.NOT_FOUND));
    }


    // Retorna 400 BAD REQUEST quando uma regra de negócio é violada
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(
            BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(criarResposta(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }


    // Retorna 400 BAD REQUEST com todos os campos inválidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(
            MethodArgumentNotValidException ex) {

        // Coleta todos os erros de validação campo a campo
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(erro -> {
            String campo = ((FieldError) erro).getField();
            String mensagem = erro.getDefaultMessage();
            erros.put(campo, mensagem);
        });

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now().toString());
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("erro", "Erro de validação");
        resposta.put("campos", erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }


    // Retorna 401 UNAUTHORIZED quando e-mail ou senha estão errados
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(
            BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(criarResposta("E-mail ou senha inválidos", HttpStatus.UNAUTHORIZED));
    }


    // Retorna 500 INTERNAL SERVER ERROR como segurança
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarResposta("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR));
    }


    // Método auxiliar que monta o corpo padrão de resposta de erro
    private Map<String, Object> criarResposta(String mensagem, HttpStatus status) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now().toString());
        resposta.put("status", status.value());
        resposta.put("erro", mensagem);
        return resposta;
    }
}