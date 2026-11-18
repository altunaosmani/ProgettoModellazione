package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

/** Applicazione JavaFX principale: gestisce lo {@link Stage}, la barra dei menu e la navigazione tra schermate. */
public class GameApp extends Application implements AppNavigator {
    private static GameController injectedController;
    private static ImageProvider injectedImages;

    private final BorderPane root = new BorderPane();
    private GameController controller;
    private AppMenuBar menuBar;
    private GameScreen gameScreen;

    /** Avvia l'interfaccia con le dipendenze già composte. */
    public static void run(GameController controller, ImageProvider images, String... args) {
        injectedController = Objects.requireNonNull(controller, "Il controller non può essere nullo");
        injectedImages = Objects.requireNonNull(images, "L'image provider non può essere nullo");
        Application.launch(GameApp.class, args);
    }

    @Override
    public void start(Stage stage) {
        if (injectedController == null || injectedImages == null)
            throw new IllegalStateException("Avvia l'applicazione tramite GameApp.run(...)");
        controller = injectedController;
        menuBar = new AppMenuBar(this);
        gameScreen = new GameScreen(controller, injectedImages, this);
        root.setTop(menuBar);

        showMainMenu();
        stage.setTitle("THE LOST ELF");
        stage.setScene(new Scene(root, 1000, 650));
        stage.show();
    }

    private void showPage(Node page, boolean saveEnabled) {
        menuBar.setSaveEnabled(saveEnabled);
        root.setCenter(page);
    }

    // ---------- AppNavigator ----------

    @Override
    public void showMainMenu() {
        showPage(new MainMenuPage(controller, this), false);
    }

    @Override
    public void startNewGame() {
        controller.newGame();
        showPage(new IntroPage(controller.intro(), this), false);
    }

    @Override
    public void requestNewGame() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Vuoi davvero iniziare una nuova partita? I progressi non salvati andranno persi.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Nuova partita");
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) startNewGame();
    }

    @Override
    public void loadGame() {
        ActionResult result = controller.load();
        if (result.success()) {
            enterGame();
            gameScreen.setStatus(result.message());
        } else {
            new Alert(Alert.AlertType.WARNING, result.message(), ButtonType.OK).showAndWait();
        }
    }

    @Override
    public void saveGame() {
        gameScreen.report(controller.save());
    }

    @Override
    public void enterGame() {
        gameScreen.setStatus("");
        showPage(gameScreen, true);
        gameScreen.refresh();
    }

    @Override
    public void showEnding() {
        showPage(new EndingPage(controller.ending(), this), false);
    }

    @Override
    public void quit() {
        Platform.exit();
    }
}