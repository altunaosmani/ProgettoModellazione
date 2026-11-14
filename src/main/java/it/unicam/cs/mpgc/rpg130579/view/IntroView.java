package it.unicam.cs.mpgc.rpg130579.view;

import java.util.List;

public record IntroView(String title, List<String> paragraphs) {
    public IntroView { paragraphs = List.copyOf(paragraphs); }
}

