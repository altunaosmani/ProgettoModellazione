package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.IntroView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class IntroPage extends VBox {

    public IntroPage(IntroView intro, AppNavigator navigator) {
        super(16);
        PageStyle.applyPaper(this);

        Label title = new Label(intro.title());
        title.setStyle("-fx-font-size: 30; -fx-font-weight: bold");
        getChildren().add(title);
        for (String paragraph : intro.paragraphs()) {
            Label label = new Label(paragraph);
            label.setWrapText(true);
            label.setMaxWidth(640);
            label.setStyle("-fx-font-size: 15");
            getChildren().add(label);
        }
        Button start = new Button("Inizia l'avventura ▶");
        start.setOnAction(e -> navigator.enterGame());
        getChildren().add(start);
    }
}