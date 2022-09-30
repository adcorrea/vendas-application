package com.adcorreajr.vendas.exception;

public class SenhaInvalidaException extends RuntimeException {

    public SenhaInvalidaException(){
        super("Senha inválida.");
    }
}
