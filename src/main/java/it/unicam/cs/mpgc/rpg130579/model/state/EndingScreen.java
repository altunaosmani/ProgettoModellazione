package it.unicam.cs.mpgc.rpg130579.model.state;

public record EndingScreen(String gameTitle, String headline, String subtitle, String message, String outcome) {
    public EndingScreen {
        if (gameTitle == null || gameTitle.isBlank()) throw new IllegalArgumentException("Il titolo del gioco non può essere vuoto");
        if (headline == null || headline.isBlank()) throw new IllegalArgumentException("Il titolo del finale non può essere vuoto");
        if (subtitle == null || subtitle.isBlank()) throw new IllegalArgumentException("Il sottotitolo non può essere vuoto");
        if (message == null || message.isBlank()) throw new IllegalArgumentException("Il messaggio non può essere vuoto");
        if (outcome == null || outcome.isBlank()) throw new IllegalArgumentException("L'esito non può essere vuoto");
    }
}
