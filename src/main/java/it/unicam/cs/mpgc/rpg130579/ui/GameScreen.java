package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import it.unicam.cs.mpgc.rpg130579.view.*;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

/**
 * Schermata principale di gioco che integra banner del livello, vista del luogo, statistiche, inventario e overlay di completamento.
 */
public class GameScreen extends BorderPane {
    private final GameController controller;
    private final AppNavigator navigator;
    private final ImageProvider images;

    private final LocationPane locationPane;
    private final InventoryPane inventoryPane;
    private final StatsPane statsPane = new StatsPane();
    private final CompletionOverlay completionOverlay;
    private final Label status = new Label();
    private final Label levelTitle = new Label();
    private final Label objective = new Label();

    public GameScreen(GameController controller, ImageProvider images, AppNavigator navigator) {
        if (controller == null) throw new IllegalArgumentException("Il controller non può essere nullo");
        if (images == null) throw new IllegalArgumentException("L'image provider non può essere nullo");
        if (navigator == null) throw new IllegalArgumentException("Il navigator non può essere nullo");
        this.controller = controller;
        this.images = images;
        this.navigator = navigator;

        locationPane = new LocationPane(images, this::onTake, this::onExamine, this::onExit,
                this::onTalk, this::onInteract);
        inventoryPane = new InventoryPane(images, this::onExamine);
        completionOverlay = new CompletionOverlay(this::onContinue);

        VBox sidebar = new VBox(statsPane, inventoryPane);
        VBox.setVgrow(inventoryPane, Priority.ALWAYS);

        ScrollPane center = new ScrollPane(locationPane);
        center.setFitToWidth(true);

        status.setStyle("-fx-padding: 8 20 8 20");
        HBox bottomBar = new HBox(10, status);
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        setTop(createBanner());
        setCenter(new StackPane(center, completionOverlay));
        setRight(sidebar);
        setBottom(bottomBar);
    }

    private VBox createBanner() {
        levelTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        objective.setWrapText(true);
        VBox box = new VBox(2, levelTitle, objective);
        box.setStyle("-fx-padding: 8 20 8 20; -fx-background-color: #d9d2c0");
        return box;
    }

    public void setStatus(String message) {
        status.setText(message == null ? "" : message);
    }

    /**
     * Mostra il messaggio dell'azione e ridisegna la schermata.
     */
    public void report(ActionResult result) {
        setStatus(result.message());
        refresh();
    }

    public void refresh() {
        GameStatus current = controller.status();
        if (current.gameOver()) {
            navigator.showEnding();
            return;
        }
        statsPane.update(controller.stats());

        LevelInfoView info = controller.levelInfo();
        levelTitle.setText("Livello " + info.number() + "/" + info.total() + " – " + info.name());
        objective.setText((info.questCompleted() ? "✔ " : "➤ ") + "Obiettivo: "
                + info.questName() + ". " + info.questDescription());

        locationPane.update(controller.currentLocation());
        inventoryPane.update(controller.inventory());
        completionOverlay.update(current, info);
    }

    private void onContinue() {
        ActionResult result = controller.advance();
        report(result);
        if (result.success() && !controller.status().gameOver()) showLevelBriefing();
    }

    private void onExit(ExitView exit) {
        report(controller.move(exit.id()));
    }

    private void onTake(ItemView item) {
        report(controller.takeItem(item.id()));
    }

    private void onExamine(ItemView item) {
        ItemDetailsDialog.show(item, images.imageFor(ImageKind.ITEM, item.id()));
    }

    private void onTalk(NpcView npc) {
        DialogueDialog.run(controller, npc.id(), images.imageFor(ImageKind.NPC, npc.id()));
        report(controller.endDialogue());
    }

    private void onInteract(MechanismView mechanism) {
        PuzzleDialog.ask(mechanism).ifPresent(answer -> report(controller.unlock(mechanism.id(), answer)));
    }

    private void showLevelBriefing() {
        LevelInfoView info = controller.levelInfo();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, info.description()
                + "\n\nObiettivo: " + info.questName() + ". " + info.questDescription(), ButtonType.OK);
        alert.setTitle("Nuovo livello");
        alert.setHeaderText("Livello " + info.number() + ": " + info.name());
        alert.showAndWait();
    }
}