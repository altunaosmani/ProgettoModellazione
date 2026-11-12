package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.controller.DefaultGameController;
import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import it.unicam.cs.mpgc.rpg130579.controller.GameManager;
import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.ExitView;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GameApp extends Application {
    private GameController controller;
    private ImageProvider images;
    private LocationPane locationPane;
    private InventoryPane inventoryPane;
    private final Label status = new Label();

    @Override
    public void start(Stage stage) {
        controller = new DefaultGameController(new GameManager(new GameFactory().newGame().state()));
        images = new ClasspathImageProvider();
        locationPane = new LocationPane(images, this::onTake, this::onExamine, this::onExit);
        inventoryPane = new InventoryPane(images, this::onExamine);

        status.setStyle("-fx-padding: 8 20 8 20");
        ScrollPane center = new ScrollPane(locationPane);
        center.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setRight(inventoryPane);
        root.setBottom(status);

        refresh();
        stage.setTitle("THE LOST ELF");
        stage.setScene(new Scene(root, 1000, 650));
        stage.show();
    }

    private void refresh() {
        locationPane.update(controller.currentLocation());
        inventoryPane.update(controller.inventory());
    }

    private void onExit(ExitView exit) {
        show(controller.move(exit.id()));
    }

    private void onTake(ItemView item) {
        show(controller.takeItem(item.id()));
    }

    private void onExamine(ItemView item) {
        ItemDetailsDialog.show(item, images.imageForItem(item.id()));
    }

    private void show(ActionResult result) {
        status.setText(result.message());
        refresh();
    }
}