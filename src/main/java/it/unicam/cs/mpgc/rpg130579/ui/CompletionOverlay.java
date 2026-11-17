package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.GameStatus;
import it.unicam.cs.mpgc.rpg130579.view.LevelInfoView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/** Overlay a tutto schermo con la card di fine livello; resta invisibile finché il livello non è completato. */
public class CompletionOverlay extends StackPane {
    private final Label title = new Label();
    private final Label text = new Label();
    private final Button nextLevelButton = new Button();

    public CompletionOverlay(Runnable onContinue) {
        title.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: #3b3322");
        text.setStyle("-fx-font-size: 16; -fx-text-fill: #3b3322");
        text.setWrapText(true);
        text.setMaxWidth(420);
        text.setTextAlignment(TextAlignment.CENTER);

        nextLevelButton.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-padding: 12 32 12 32;"
                + "-fx-background-color: #6b8e4e; -fx-text-fill: white; -fx-background-radius: 8");
        nextLevelButton.setOnAction(e -> onContinue.run());

        VBox card = new VBox(16, title, text, nextLevelButton);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        card.setStyle("-fx-padding: 32 48 32 48; -fx-background-color: #ece7da; -fx-background-radius: 14;"
                + "-fx-border-color: #6b8e4e; -fx-border-width: 3; -fx-border-radius: 14;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 20, 0.3, 0, 4)");

        getChildren().add(card);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: rgba(0,0,0,0.45)");
        setVisible(false); // invisibile = non intercetta i click
    }

    public void update(GameStatus status, LevelInfoView info) {
        boolean show = status.levelCompleted();
        if (show) {
            boolean last = info.number() == info.total();
            title.setText("Hai completato il livello!");
            text.setText(last
                    ? "La tua ricerca è giunta al termine."
                    : "Il prossimo luogo ti aspetta.");
            nextLevelButton.setText(last ? "Concludi l'avventura ▶" : "Vai al livello successivo ▶");
        }
        setVisible(show);
    }
}