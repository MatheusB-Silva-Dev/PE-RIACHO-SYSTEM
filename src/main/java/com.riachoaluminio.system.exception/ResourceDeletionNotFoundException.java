package com.riachoaluminio.system.exception;

public class ResourceDeletionNotFoundException extends RuntimeException {
    public ResourceDeletionNotFoundException() {
        super("Não há recurso a ser deletado para o ID fornecido");
    }
}
