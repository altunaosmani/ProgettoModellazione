package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.EndingView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class EndingPage extends VBox {

    public EndingPage(Optional<EndingView> ending, AppNavigator navigator) {
        super(16);
        PageStyle.applyPaper(this);

        ending.ifPresentOrElse(this::fill, () -> getChildren().add(new Label("Partita terminata.")));

        Button again = new Button("Nuova partita");
        again.setOnAction(e -> navigator.startNewGame());
        Button menu = new Button("Menu principale");
        menu.setOnAction(e -> navigator.showMainMenu());
        Button quit = new Button("Esci");
        quit.setOnAction(e -> navigator.quit());
        HBox buttons = new HBox(10, again, menu, quit);
        buttons.setAlignment(Pos.CENTER);
        getChildren().add(buttons);
    }

    private void fill(EndingView ending) {
        Label game = new Label(ending.gameTitle());
        game.setStyle("-fx-font-size: 34; -fx-font-weight: bold");
        Label headline = new Label(ending.headline());
        headline.setStyle("-fx-font-size: 24");
        Label subtitle = new Label(ending.subtitle());
        subtitle.setStyle("-fx-font-size: 18");
        Label message = new Label("“" + ending.message() + "”");
        message.setWrapText(true);
        message.setMaxWidth(600);
        message.setStyle("-fx-font-style: italic; -fx-font-size: 15");
        Label outcome = new Label(ending.outcome());
        outcome.setStyle("-fx-font-size: 28; -fx-font-weight: bold");
        getChildren().addAll(game, headline, subtitle, message, outcome);
    }
}