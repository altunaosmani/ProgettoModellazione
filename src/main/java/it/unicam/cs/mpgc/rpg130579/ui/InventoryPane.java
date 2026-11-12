package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class InventoryPane extends VBox {
    private final ImageProvider images;
    private final Consumer<ItemView> onExamine;
    private final TilePane tiles = new TilePane(8, 8);

    public InventoryPane(ImageProvider images, Consumer<ItemView> onExamine) {
        super(8);
        this.images = images;
        this.onExamine = onExamine;
        setPrefWidth(270);
        setStyle("-fx-padding: 12; -fx-background-color: #ece7da");

        Label title = new Label("Inventario");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold");
        tiles.setPrefColumns(2);

        ScrollPane scroll = new ScrollPane(tiles);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent");
        getChildren().addAll(title, scroll);
    }

    public void update(List<ItemView> items) {
        tiles.getChildren().clear();
        if (items.isEmpty()) {
            tiles.getChildren().add(new Label("Vuoto"));
            return;
        }
        for (ItemView item : items) {
            tiles.getChildren().add(new ItemCard(item, images.imageForItem(item.id()), null, null, onExamine));
        }
    }
}
