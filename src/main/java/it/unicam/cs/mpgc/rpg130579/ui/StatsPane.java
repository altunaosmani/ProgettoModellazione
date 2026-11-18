package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.StatsView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Pannello laterale che visualizza gli attributi effettivi del giocatore e i relativi bonus da oggetti.
 */
public class StatsPane extends VBox {

    public StatsPane() {
        super(4);
        setPrefWidth(270);
        setStyle("-fx-padding: 12 12 4 12; -fx-background-color: #ece7da");
    }

    public void update(StatsView stats) {
        getChildren().clear();
        Label title = new Label("Statistiche");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold");
        getChildren().add(title);
        stats.values().forEach((name, value) -> {
            int bonus = stats.bonuses().getOrDefault(name, 0);
            String suffix = bonus > 0 ? "  (+" + bonus + " da oggetti)" : "";
            getChildren().add(new Label(name + ": " + value + suffix));
        });
    }
}