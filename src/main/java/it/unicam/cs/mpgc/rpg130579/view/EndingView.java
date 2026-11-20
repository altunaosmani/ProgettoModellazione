package it.unicam.cs.mpgc.rpg130579.view;

/**
 * Dati di presentazione della schermata finale raggiunta al termine della partita.
 *
 * @param gameTitle titolo del gioco
 * @param headline  intestazione principale dell'esito
 * @param subtitle  sottotitolo descrittivo del finale ottenuto
 * @param message   testo narrativo conclusivo
 * @param outcome   etichetta sintetica del risultato (es. VITTORIA)
 */
public record EndingView(String gameTitle, String headline, String subtitle, String message, String outcome) {
}