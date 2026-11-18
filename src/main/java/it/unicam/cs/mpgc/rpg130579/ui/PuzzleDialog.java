package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.MechanismView;
import it.unicam.cs.mpgc.rpg130579.view.PuzzleView;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Finestra di dialogo modale per la risoluzione di enigmi a scelta o a sequenza. */
public final class PuzzleDialog {

    /**
     * @return la risposta da passare a unlock(); lista vuota se non serve input;
     *         Optional.empty() se il giocatore annulla o non può ancora provare
     */
    public static Optional<List<String>> ask(MechanismView mechanism) {
        PuzzleView puzzle = mechanism.puzzle();
        if (puzzle == null || puzzle.answerSize() == 0) return Optional.of(List.of());

        if (puzzle.choices().size() < puzzle.answerSize()) {
            String text = "Ti manca ancora qualcosa per risolvere questo enigma.";
            if (!mechanism.hint().isBlank()) text += "\n\nIndizio: " + mechanism.hint();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.OK);
            alert.setHeaderText(mechanism.name());
            alert.showAndWait();
            return Optional.empty();
        }
        return pick(mechanism, puzzle);
    }

    private static Optional<List<String>> pick(MechanismView mechanism, PuzzleView puzzle) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle(mechanism.name());
        dialog.setHeaderText(mechanism.name());

        List<String> pickedIds = new ArrayList<>();
        List<String> pickedTexts = new ArrayList<>();
        List<Button> buttons = new ArrayList<>();
        Label progress = new Label();
        progress.setStyle("-fx-font-style: italic");

        Label description = new Label(puzzle.description());
        description.setWrapText(true);
        description.setMinHeight(Region.USE_PREF_SIZE); // evita il testo tagliato con "..."

        FlowPane options = new FlowPane(8, 8);
        for (Map.Entry<String, String> entry : puzzle.choices().entrySet()) {
            Button button = new Button(entry.getValue());
            buttons.add(button);
            button.setOnAction(e -> {
                pickedIds.add(entry.getKey());
                pickedTexts.add(entry.getValue());
                button.setDisable(true);
                progress.setText(String.join(" ", pickedTexts));
                if (pickedIds.size() == puzzle.answerSize()) {
                    dialog.setResult(new ArrayList<>(pickedIds));
                    dialog.close();
                }
            });
            options.getChildren().add(button);
        }

        VBox content = new VBox(10, description);
        if (!mechanism.hint().isBlank()) {
            Label hint = new Label("Indizio: " + mechanism.hint());
            hint.setWrapText(true);
            hint.setMinHeight(Region.USE_PREF_SIZE);
            hint.setStyle("-fx-font-style: italic; -fx-text-fill: #5a4e2f");
            content.getChildren().add(hint);
        }
        content.getChildren().add(options);
        content.setPadding(new Insets(10));
        content.setPrefWidth(460);

        if (puzzle.answerSize() > 1) {
            Button reset = new Button("Ricomincia");
            reset.setOnAction(e -> {
                pickedIds.clear();
                pickedTexts.clear();
                progress.setText("");
                buttons.forEach(b -> b.setDisable(false));
            });
            content.getChildren().addAll(progress, reset);
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        return dialog.showAndWait();
    }

    private PuzzleDialog() {
    }
}