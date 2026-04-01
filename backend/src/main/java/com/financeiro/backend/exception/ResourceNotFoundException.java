package com.financeiro.backend.exception;

// Exceção lançada quando um recurso não é encontrado no banco
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}