package it.unicam.cs.mpgc.rpg130579.controller;

final class ErrorMessages {
    private ErrorMessages() {
    }

    static String of(RuntimeException e) {
        return e.getMessage() == null ? "Operazione non riuscita." : e.getMessage();
    }
}