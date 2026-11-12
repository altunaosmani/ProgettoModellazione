package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

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

        getChildren().addAll(createIcon(item, image), name, buttons);
    }

    private Node createIcon(ItemView item, Optional<Image> image) {
        if (image.isPresent()) {
            ImageView view = new ImageView(image.get());
            view.setFitWidth(ICON_SIZE);
            view.setFitHeight(ICON_SIZE);
            view.setPreserveRatio(true);
            return view;
        }
        Rectangle background = new Rectangle(ICON_SIZE, ICON_SIZE);
        background.setArcWidth(12);
        background.setArcHeight(12);
        background.setStyle("-fx-fill: #d9d2c0");
        Label initial = new Label(item.name().substring(0, 1).toUpperCase());
        initial.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #6b5f45");
        return new StackPane(background, initial);
    }
}