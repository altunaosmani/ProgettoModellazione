package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.content.GameSetup;
import it.unicam.cs.mpgc.rpg130579.controller.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameApp extends Application {
    @Override
    public void start(Stage stage) {
        GameSetup setup = new GameFactory().newGame();
        GameManager game = new GameManager(setup.state());
        var location = setup.state().getPlayer().getCurrentLocation();

        VBox root = new VBox(10, new Label(location.getName()), new Label(location.getDescription()));
        root.setStyle("-fx-padding: 20");
        stage.setTitle("THE LOST ELF");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
}
