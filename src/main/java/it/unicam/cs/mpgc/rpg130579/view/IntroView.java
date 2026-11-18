package it.unicam.cs.mpgc.rpg130579.view;

import java.util.List;

/** Dati di presentazione della schermata introduttiva. */
public record IntroView(String title, List<String> paragraphs) {
    public IntroView { paragraphs = List.copyOf(paragraphs); }
}

