package com.financeiro.backend.exception;

// Exceção lançada quando uma regra de negócio é violada
public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }
}