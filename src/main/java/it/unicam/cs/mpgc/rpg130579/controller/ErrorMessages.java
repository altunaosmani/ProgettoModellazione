package it.unicam.cs.mpgc.rpg130579.controller;

/**
 * Classe di utilità interna per convertire le eccezioni di runtime in messaggi di errore
 * leggibili dall'utente finale.
 */
public final class ErrorMessages {
    private ErrorMessages() {
    }

    /**
     * Estrae il messaggio dall'eccezione fornita, restituendo un testo predefinito se assente.
     *
     * @param e eccezione catturata durante l'esecuzione di un'azione
     * @return messaggio descrittivo non nullo
     */
    static String of(RuntimeException e) {
        return e.getMessage() == null ? "Operazione non riuscita." : e.getMessage();
    }
}