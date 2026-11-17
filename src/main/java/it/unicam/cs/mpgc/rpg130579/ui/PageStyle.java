package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/** Stile comune delle schermate a tutta pagina (menu, prologo, finale). */
final class PageStyle {
    private static final String PAPER = "-fx-padding: 40; -fx-background-color: #ece7da";

    static void applyPaper(VBox box) {
        box.setAlignment(Pos.CENTER);
        box.setStyle(PAPER);
    }

    private PageStyle() {
    }
}