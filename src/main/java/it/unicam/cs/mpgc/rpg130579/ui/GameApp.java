package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.controller.DefaultGameController;
import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import it.unicam.cs.mpgc.rpg130579.persistence.JsonSaveRepository;
import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.EndingView;
import it.unicam.cs.mpgc.rpg130579.view.ExitView;
import it.unicam.cs.mpgc.rpg130579.view.GameStatus;
import it.unicam.cs.mpgc.rpg130579.view.IntroView;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LevelInfoView;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameApp extends Application {
    private static final String PAPER = "-fx-padding: 40; -fx-background-color: #ece7da";

    private GameController controller;
    private ImageProvider images;
    private LocationPane locationPane;
    private InventoryPane inventoryPane;
    private final Label status = new Label();
    private final Label levelTitle = new Label();
    private final Label objective = new Label();
    private final BorderPane root = new BorderPane();
    private final VBox top = new VBox();
    private MenuBar menuBar;
    private MenuItem saveItem;
    private VBox banner;
    private ScrollPane center;
    private StackPane centerStack;
    private HBox bottomBar;

    // Banner di fine livello
    private StackPane completionOverlay;
    private final Label completionTitle = new Label();
    private final Label completionText = new Label();
    private final Button nextLevelButton = new Button();

    @Override
    public void start(Stage stage) {
        controller = new DefaultGameController(new GameFactory()::newGame, new JsonSaveRepository());
        images = new ClasspathImageProvider();
        locationPane = new LocationPane(images, this::onTake, this::onExamine, this::onExit,
                this::onTalk, this::onInteract);
        inventoryPane = new InventoryPane(images, this::onExamine);

        center = new ScrollPane(locationPane);
        center.setFitToWidth(true);
        completionOverlay = createCompletionOverlay();
        centerStack = new StackPane(center, completionOverlay);

        status.setStyle("-fx-padding: 8 20 8 20");
        bottomBar = new HBox(10, status);
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        banner = createBanner();
        menuBar = createMenu();
        root.setTop(top);

        showMainMenu();
        stage.setTitle("THE LOST ELF");
        stage.setScene(new Scene(root, 1000, 650));
        stage.show();
    }

    // ---------- Costruzione componenti ----------

    private VBox createBanner() {
        levelTitle.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        objective.setWrapText(true);
        VBox box = new VBox(2, levelTitle, objective);
        box.setStyle("-fx-padding: 8 20 8 20; -fx-background-color: #d9d2c0");
        return box;
    }

    private MenuBar createMenu() {
        MenuItem newGame = new MenuItem("Nuova partita");
        newGame.setOnAction(e -> onNewGame());
        saveItem = new MenuItem("Salva");
        saveItem.setOnAction(e -> show(controller.save()));
        MenuItem load = new MenuItem("Carica");
        load.setOnAction(e -> onLoad());
        MenuItem exit = new MenuItem("Esci");
        exit.setOnAction(e -> Platform.exit());
        return new MenuBar(new Menu("Partita", null, newGame, saveItem, load, exit));
    }

    private VBox screen() {
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setStyle(PAPER);
        return box;
    }

    /** Overlay a tutto schermo con la card centrale; resta invisibile finché il livello non è completato. */
    private StackPane createCompletionOverlay() {
        completionTitle.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: #3b3322");
        completionText.setStyle("-fx-font-size: 16; -fx-text-fill: #3b3322");
        completionText.setWrapText(true);
        completionText.setMaxWidth(420);
        completionText.setTextAlignment(TextAlignment.CENTER);

        nextLevelButton.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-padding: 12 32 12 32;"
                + "-fx-background-color: #6b8e4e; -fx-text-fill: white; -fx-background-radius: 8");
        nextLevelButton.setOnAction(e -> onContinue());

        VBox card = new VBox(16, completionTitle, completionText, nextLevelButton);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        card.setStyle("-fx-padding: 32 48 32 48; -fx-background-color: #ece7da; -fx-background-radius: 14;"
                + "-fx-border-color: #6b8e4e; -fx-border-width: 3; -fx-border-radius: 14;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 20, 0.3, 0, 4)");

        StackPane overlay = new StackPane(card);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45)");
        overlay.setVisible(false); // invisibile = non intercetta i click
        return overlay;
    }

    private void updateCompletionOverlay(GameStatus current, LevelInfoView info) {
        boolean show = current.levelCompleted();
        if (show) {
            boolean last = info.number() == info.total();
            completionTitle.setText("Hai completato il livello!");
            completionText.setText(last
                    ? "La tua ricerca è giunta al termine."
                    : "Il prossimo luogo ti aspetta.");
            nextLevelButton.setText(last ? "Concludi l'avventura ▶" : "Vai al livello successivo ▶");
        }
        completionOverlay.setVisible(show);
    }

    // ---------- Schermate ----------

    private void showScreen(VBox box) {
        top.getChildren().setAll(menuBar);
        saveItem.setDisable(true);
        root.setCenter(box);
        root.setRight(null);
        root.setBottom(null);
    }

    private void showMainMenu() {
        VBox box = screen();
        Label title = new Label("THE LOST ELF");
        title.setStyle("-fx-font-size: 44; -fx-font-weight: bold");
        Label subtitle = new Label("Segui le tracce di Elara");
        subtitle.setStyle("-fx-font-size: 16; -fx-font-style: italic");

        Button newGame = new Button("Nuova partita");
        newGame.setOnAction(e -> startNewGame());
        Button continueGame = new Button("Continua");
        continueGame.setDisable(!controller.hasSave());
        continueGame.setOnAction(e -> onLoad());
        Button quit = new Button("Esci");
        quit.setOnAction(e -> Platform.exit());
        for (Button b : new Button[]{newGame, continueGame, quit}) b.setPrefWidth(200);

        box.getChildren().addAll(title, subtitle, newGame, continueGame, quit);
        showScreen(box);
    }

    private void showIntro() {
        IntroView intro = controller.intro();
        VBox box = screen();
        Label title = new Label(intro.title());
        title.setStyle("-fx-font-size: 30; -fx-font-weight: bold");
        box.getChildren().add(title);
        for (String paragraph : intro.paragraphs()) {
            Label label = new Label(paragraph);
            label.setWrapText(true);
            label.setMaxWidth(640);
            label.setStyle("-fx-font-size: 15");
            box.getChildren().add(label);
        }
        Button start = new Button("Inizia l'avventura ▶");
        start.setOnAction(e -> enterGame());
        box.getChildren().add(start);
        showScreen(box);
    }

    private void enterGame() {
        status.setText("");
        refresh();
    }

    private void refresh() {
        GameStatus current = controller.status();
        if (current.gameOver()) {
            showEnding();
            return;
        }
        top.getChildren().setAll(menuBar, banner);
        saveItem.setDisable(false);
        root.setCenter(centerStack);
        root.setRight(inventoryPane);
        root.setBottom(bottomBar);

        LevelInfoView info = controller.levelInfo();
        levelTitle.setText("Livello " + info.number() + "/" + info.total() + " – " + info.name());
        objective.setText((info.questCompleted() ? "✔ " : "➤ ") + "Obiettivo: "
                + info.questName() + ". " + info.questDescription());

        locationPane.update(controller.currentLocation());
        inventoryPane.update(controller.inventory());
        updateCompletionOverlay(current, info);
    }

    private void showLevelBriefing() {
        LevelInfoView info = controller.levelInfo();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, info.description()
                + "\n\nObiettivo: " + info.questName() + ". " + info.questDescription(), ButtonType.OK);
        alert.setTitle("Nuovo livello");
        alert.setHeaderText("Livello " + info.number() + ": " + info.name());
        alert.showAndWait();
    }

    private void showEnding() {
        VBox box = screen();
        controller.ending().ifPresentOrElse(ending -> fillEnding(box, ending),
                () -> box.getChildren().add(new Label("Partita terminata.")));

        Button again = new Button("Nuova partita");
        again.setOnAction(e -> startNewGame());
        Button menu = new Button("Menu principale");
        menu.setOnAction(e -> showMainMenu());
        Button quit = new Button("Esci");
        quit.setOnAction(e -> Platform.exit());
        HBox buttons = new HBox(10, again, menu, quit);
        buttons.setAlignment(Pos.CENTER);
        box.getChildren().add(buttons);
        showScreen(box);
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

    // ---------- Azioni ----------

    private void startNewGame() {
        controller.newGame();
        showIntro();
    }

    private void onNewGame() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Vuoi davvero iniziare una nuova partita? I progressi non salvati andranno persi.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Nuova partita");
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) startNewGame();
    }

    private void onLoad() {
        ActionResult result = controller.load();
        if (result.success()) {
            enterGame();
            status.setText(result.message());
        } else {
            new Alert(Alert.AlertType.WARNING, result.message(), ButtonType.OK).showAndWait();
        }
    }

    private void onContinue() {
        ActionResult result = controller.advance();
        show(result);
        if (result.success() && !controller.status().gameOver()) showLevelBriefing();
    }

    private void onExit(ExitView exit) {
        show(controller.move(exit.id()));
    }

    private void onTake(ItemView item) {
        show(controller.takeItem(item.id()));
    }

    private void onExamine(ItemView item) {
        ItemDetailsDialog.show(item, images.imageFor(ImageKind.ITEM, item.id()));
    }

    private void onTalk(NpcView npc) {
        try {
            DialogueDialog.run(controller, npc.id(), images.imageFor(ImageKind.NPC, npc.id()));
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