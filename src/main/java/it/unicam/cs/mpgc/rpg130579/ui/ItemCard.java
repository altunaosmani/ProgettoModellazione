package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Optional;
import java.util.function.Consumer;

public class ItemCard extends VBox {
    private static final double ICON_SIZE = 72;

    /**
     * @param actionLabel testo del pulsante principale (es. "Raccogli"); null se non serve
     * @param onAction    callback del pulsante principale; può essere null
     */
    public ItemCard(ItemView item, Optional<Image> image, String actionLabel,
                    Consumer<ItemView> onAction, Consumer<ItemView> onExamine) {
        super(6);
        setAlignment(Pos.CENTER);
        setPrefWidth(110);
        setStyle("-fx-padding: 8; -fx-background-color: #ffffff; -fx-background-radius: 8;"
                + "-fx-border-color: #cfcfcf; -fx-border-radius: 8;");

        Label name = new Label(item.name());
        name.setWrapText(true);
        name.setMaxWidth(100);
        name.setAlignment(Pos.CENTER);
        name.setStyle("-fx-font-size: 11; -fx-text-alignment: center");

        HBox buttons = new HBox(4);
        buttons.setAlignment(Pos.CENTER);
        Button examine = new Button("Esamina");
        examine.setOnAction(e -> onExamine.accept(item));
        buttons.getChildren().add(examine);
        if (actionLabel != null && onAction != null) {
            Button action = new Button(actionLabel);
            action.setOnAction(e -> onAction.accept(item));
            buttons.getChildren().add(action);
        }

        getChildren().addAll(IconFactory.create(item.name(), image, ICON_SIZE), name, buttons);    }
}