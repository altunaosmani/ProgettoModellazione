package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.NpcView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.function.Consumer;

public class NpcCard extends VBox {
    private static final double ICON_SIZE = 72;

    public NpcCard(NpcView npc, Optional<Image> portrait, Consumer<NpcView> onTalk) {
        super(6);
        setAlignment(Pos.CENTER);
        setPrefWidth(110);
        setStyle("-fx-padding: 8; -fx-background-color: #ffffff; -fx-background-radius: 8;"
                + "-fx-border-color: #cfcfcf; -fx-border-radius: 8;");

        Label name = new Label(npc.name());
        name.setStyle("-fx-font-size: 12; -fx-font-weight: bold");
        Button talk = new Button("Parla");
        talk.setTooltip(new Tooltip(npc.description()));
        talk.setOnAction(e -> onTalk.accept(npc));

        getChildren().addAll(IconFactory.create(npc.name(), portrait, ICON_SIZE), name, talk);
    }
}
