package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.controller.DefaultGameController;
import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import it.unicam.cs.mpgc.rpg130579.persistence.JsonSaveRepository;
import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.EndingView;
import it.unicam.cs.mpgc.rpg130579.view.ExitView;
import it.unicam.cs.mpgc.rpg130579.view.GameStatus;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.MechanismView;
import it.unicam.cs.mpgc.rpg130579.view.NpcView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameApp extends Application {
    private GameController controller;
    private ImageProvider images;
    private LocationPane locationPane;
    private InventoryPane inventoryPane;
    private final Label status = new Label();
    private final Button continueButton = new Button("Continua ▶");
    private final BorderPane root = new BorderPane();
    private ScrollPane center;
    private HBox bottomBar;

    @Override
    public void start(Stage stage) {
        controller = new DefaultGameController(new GameFactory()::newGame, new JsonSaveRepository());
        images = new ClasspathImageProvider();
        locationPane = new LocationPane(images, this::onTake, this::onExamine, this::onExit,
                this::onTalk, this::onInteract);
        inventoryPane = new InventoryPane(images, this::onExamine);

        center = new ScrollPane(locationPane);
        center.setFitToWidth(true);

        status.setStyle("-fx-padding: 8 20 8 20");
        continueButton.setOnAction(e -> show(controller.advance()));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomBar = new HBox(10, status, spacer, continueButton);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setStyle("-fx-padding: 0 20 0 0");

        root.setTop(createMenu());
        refresh();
        stage.setTitle("THE LOST ELF");
        stage.setScene(new Scene(root, 1000, 650));
        stage.show();
    }

    private MenuBar createMenu() {
        MenuItem newGame = new MenuItem("Nuova partita");
        newGame.setOnAction(e -> onNewGame());
        MenuItem save = new MenuItem("Salva");
        save.setOnAction(e -> show(controller.save()));
        MenuItem load = new MenuItem("Carica");
        load.setOnAction(e -> show(controller.load()));
        MenuItem exit = new MenuItem("Esci");
        exit.setOnAction(e -> Platform.exit());
        return new MenuBar(new Menu("Partita", null, newGame, save, load, exit));
    }

    private void refresh() {
        GameStatus current = controller.status();
        if (current.gameOver()) {
            showEnding();
            return;
        }
        root.setCenter(center);
        root.setRight(inventoryPane);
        root.setBottom(bottomBar);
        locationPane.update(controller.currentLocation());
        inventoryPane.update(controller.inventory());
        continueButton.setVisible(current.levelCompleted());
        continueButton.setManaged(current.levelCompleted());
    }

    private void showEnding() {
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-padding: 40; -fx-background-color: #ece7da");

        controller.ending().ifPresentOrElse(ending -> fillEnding(box, ending),
                () -> box.getChildren().add(new Label("Partita terminata.")));

        Button again = new Button("Nuova partita");
        again.setOnAction(e -> onNewGame());
        Button quit = new Button("Esci");
        quit.setOnAction(e -> Platform.exit());
        HBox buttons = new HBox(10, again, quit);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().add(buttons);

        root.setCenter(box);
        root.setRight(null);
        root.setBottom(null);
    }

    private void fillEnding(VBox box, EndingView ending) {
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
        box.getChildren().addAll(game, headline, subtitle, message, outcome);
    }

    private void onNewGame() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Vuoi davvero iniziare una nuova partita? I progressi non salvati andranno persi.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Nuova partita");
        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;
        controller.newGame();
        status.setText("");
        refresh();
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

    private void onTalk(NpcView npc) {
        try {
            DialogueDialog.run(controller, npc.id());
            show(ActionResult.ok());
        } catch (IllegalArgumentException | IllegalStateException e) {
            show(ActionResult.fail(e.getMessage()));
        }
    }

    private void onInteract(MechanismView mechanism) {
        PuzzleDialog.ask(mechanism).ifPresent(answer -> show(controller.unlock(mechanism.id(), answer)));
    }

    private void show(ActionResult result) {
        status.setText(result.message());
        refresh();
    }
}