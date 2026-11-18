package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

/** Factory interna per generare nodi grafici icona con fallback sull'iniziale in caso di immagine mancante. */
public final class IconFactory {

    static Node create(String label, Optional<Image> image, double size) {
        if (image.isPresent()) {
            ImageView view = new ImageView(image.get());
            view.setFitWidth(size);
            view.setFitHeight(size);
            view.setPreserveRatio(true);
            return view;
        }
        Rectangle background = new Rectangle(size, size);
        background.setArcWidth(12);
        background.setArcHeight(12);
        background.setStyle("-fx-fill: #d9d2c0");
        Label initial = new Label(label.substring(0, 1).toUpperCase());
        initial.setStyle("-fx-font-size: " + (size * 0.4) + "; -fx-font-weight: bold; -fx-text-fill: #6b5f45");
        return new StackPane(background, initial);
    }

    private IconFactory() { }
}
