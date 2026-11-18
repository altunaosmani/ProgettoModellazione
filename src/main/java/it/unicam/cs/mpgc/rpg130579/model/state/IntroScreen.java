package it.unicam.cs.mpgc.rpg130579.model.state;

import java.util.List;

/** DTO di dominio che descrive il titolo e i paragrafi della schermata introduttiva. */
public record IntroScreen(String title, List<String> paragraphs) {
    public IntroScreen {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Il titolo dell'introduzione non può essere vuoto");
        if (paragraphs == null || paragraphs.isEmpty()) throw new IllegalArgumentException("L'introduzione deve avere almeno un paragrafo");
        paragraphs = List.copyOf(paragraphs);
    }
}
