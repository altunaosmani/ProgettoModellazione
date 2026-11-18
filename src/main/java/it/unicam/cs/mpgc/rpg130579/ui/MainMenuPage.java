package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Schermata del menu principale con avvio nuova partita e caricamento salvataggio.
 */
public class MainMenuPage extends VBox {

    public MainMenuPage(GameController controller, AppNavigator navigator) {
        super(16);
        PageStyle.applyPaper(this);

        Label title = new Label("THE LOST ELF");
        title.setStyle("-fx-font-size: 44; -fx-font-weight: bold");
        Label subtitle = new Label("Segui le tracce di Elara");
        subtitle.setStyle("-fx-font-size: 16; -fx-font-style: italic");

        Button newGame = new Button("Nuova partita");
        newGame.setOnAction(e -> navigator.startNewGame());
        Button continueGame = new Button("Continua");
        continueGame.setDisable(!controller.hasSave());
        continueGame.setOnAction(e -> navigator.loadGame());
        Button quit = new Button("Esci");
        quit.setOnAction(e -> navigator.quit());
        for (Button b : new Button[]{newGame, continueGame, quit}) b.setPrefWidth(200);

        getChildren().addAll(title, subtitle, newGame, continueGame, quit);
    }
}