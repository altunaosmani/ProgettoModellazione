package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.GameStatus;
import it.unicam.cs.mpgc.rpg130579.view.LevelInfoView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Banner non bloccante mostrato sopra la scena quando il livello è completato.
 * Il giocatore può proseguire subito oppure continuare a esplorare: in quel caso
 * il banner si riduce a una riga con il solo pulsante per proseguire.
 */
public class CompletionBanner extends HBox {
    private final Label text = new Label();
    private final Button keepExploring = new Button("Continua a esplorare");
    private final Button proceed = new Button();
    private boolean dismissed;

    public CompletionBanner(Runnable onContinue) {
        super(12);
        setAlignment(Pos.CENTER_LEFT);
        setStyle("-fx-padding: 8 20 8 20; -fx-background-color: #dfe8d3;"
                + "-fx-border-color: #6b8e4e; -fx-border-width: 0 0 2 0");

        text.setWrapText(true);
        text.setStyle("-fx-font-weight: bold; -fx-text-fill: #3b3322");
        text.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(text, Priority.ALWAYS);

        keepExploring.setOnAction(e -> {
            dismissed = true;
            applyMode();
        });
        proceed.setStyle("-fx-font-weight: bold; -fx-background-color: #6b8e4e; -fx-text-fill: white");
        proceed.setOnAction(e -> onContinue.run());

        getChildren().addAll(text, keepExploring, proceed);
        hide();
    }

    public void update(GameStatus status, LevelInfoView info) {
        if (!status.levelCompleted()) {
            dismissed = false;
            hide();
            return;
        }
        setVisible(true);
        setManaged(true);
        boolean last = info.number() == info.total();
        proceed.setText(last ? "Concludi l'avventura ▶" : "Vai al livello successivo ▶");
        applyMode(last);
    }

    private void applyMode() {
        applyMode(proceed.getText().startsWith("Concludi"));
    }

    private void applyMode(boolean last) {
        if (dismissed) {
            text.setText("✔ Livello completato");
        } else {
            text.setText(last
                    ? "Hai completato l'ultimo livello! Puoi concludere l'avventura o continuare a esplorare."
                    : "Livello completato! Puoi continuare a esplorare: passando al livello successivo non potrai tornare indietro.");
        }
        keepExploring.setVisible(!dismissed);
        keepExploring.setManaged(!dismissed);
    }

    private void hide() {
        setVisible(false);
        setManaged(false);
    }
}