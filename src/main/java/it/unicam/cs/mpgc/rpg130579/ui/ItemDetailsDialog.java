package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.util.Optional;

/**
 * Finestra di dialogo modale che mostra i dettagli e il contenuto leggibile di un oggetto.
 */
public final class ItemDetailsDialog {

    private ItemDetailsDialog() {
    }

    public static void show(ItemView item, Optional<Image> image) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(item.name());
        alert.setHeaderText(item.name());
        alert.setContentText(item.details());
        alert.getButtonTypes().add(ButtonType.CLOSE);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        image.ifPresent(img -> {
            ImageView view = new ImageView(img);
            view.setFitWidth(96);
            view.setPreserveRatio(true);
            alert.setGraphic(view);
        });
        alert.showAndWait();
    }
}