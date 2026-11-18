package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.controller.DialogueController;
import it.unicam.cs.mpgc.rpg130579.view.DialogueView;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

/** Finestra di dialogo modale che gestisce l'interazione a turni con un NPC. */
public final class DialogueDialog {
    private static final double PORTRAIT_SIZE = 64;

    public static void run(DialogueController controller, String npcId, Optional<Image> portrait) {
        DialogueView view = controller.talkTo(npcId);
        while (true) {
            if (view.choices().isEmpty()) {
                if (!view.lines().isEmpty()) showLines(view, portrait);
                return;
            }
            int index = askChoice(view, portrait);
            if (index < 0) return; // finestra chiusa
            view = controller.choose(index);
        }
    }

    private static void showLines(DialogueView view, Optional<Image> portrait) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(view.speaker());
        dialog.setHeaderText(view.speaker());
        dialog.setGraphic(IconFactory.create(view.speaker(), portrait, PORTRAIT_SIZE));
        dialog.getDialogPane().setContent(textBox(view.lines()));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private static int askChoice(DialogueView view, Optional<Image> portrait) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle(view.speaker());
        dialog.setHeaderText(view.speaker());
        dialog.setGraphic(IconFactory.create(view.speaker(), portrait, PORTRAIT_SIZE));

        VBox box = textBox(view.lines());
        for (int i = 0; i < view.choices().size(); i++) {
            int index = i;
            Button button = new Button(view.choices().get(i));
            button.setWrapText(true);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(e -> {
                dialog.setResult(index);
                dialog.close();
            });
            box.getChildren().add(button);
        }
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        return dialog.showAndWait().orElse(-1);
    }

    private static VBox textBox(List<String> lines) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(460);
        for (String line : lines) {
            Label label = new Label(line);
            label.setWrapText(true);
            box.getChildren().add(label);
        }
        return box;
    }

    private DialogueDialog() {
    }
}