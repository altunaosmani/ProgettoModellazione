package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Utility interna per applicare lo stile grafico comune alle schermate a pagina intera.
 */
public final class PageStyle {
    private static final String PAPER = "-fx-padding: 40; -fx-background-color: #ece7da";

    private PageStyle() {
    }

    static void applyPaper(VBox box) {
        box.setAlignment(Pos.CENTER);
        box.setStyle(PAPER);
    }
}