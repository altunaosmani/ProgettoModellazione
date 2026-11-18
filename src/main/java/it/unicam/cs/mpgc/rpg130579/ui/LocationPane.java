package it.unicam.cs.mpgc.rpg130579.ui;

import it.unicam.cs.mpgc.rpg130579.view.ExitView;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LocationView;
import it.unicam.cs.mpgc.rpg130579.view.MechanismView;
import it.unicam.cs.mpgc.rpg130579.view.NpcView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/** Pannello centrale che visualizza descrizione, dettagli condizionali, NPC, meccanismi, oggetti e uscite del luogo corrente. */
public class LocationPane extends VBox {
    private static final double LOCATION_ICON_SIZE = 64;

    private final ImageProvider images;
    private final Consumer<ItemView> onTake;
    private final Consumer<ItemView> onExamine;
    private final Consumer<ExitView> onExit;
    private final Consumer<NpcView> onTalk;
    private final Consumer<MechanismView> onInteract;

    public LocationPane(ImageProvider images, Consumer<ItemView> onTake, Consumer<ItemView> onExamine,
                        Consumer<ExitView> onExit, Consumer<NpcView> onTalk,
                        Consumer<MechanismView> onInteract) {
        super(12);
        this.images = images;
        this.onTake = onTake;
        this.onExamine = onExamine;
        this.onExit = onExit;
        this.onTalk = onTalk;
        this.onInteract = onInteract;
        setStyle("-fx-padding: 20");
    }

    public void update(LocationView location) {
        getChildren().clear();
        getChildren().add(createHeader(location));

        if (!location.npcs().isEmpty()) {
            getChildren().add(sectionTitle("Personaggi"));
            FlowPane npcs = new FlowPane(10, 10);
            for (NpcView npc : location.npcs()) {
                npcs.getChildren().add(new NpcCard(npc, images.imageFor(ImageKind.NPC, npc.id()), onTalk));
            }
            getChildren().add(npcs);
        }

        if (!location.mechanisms().isEmpty()) {
            getChildren().add(sectionTitle("Da esaminare"));
            FlowPane mechanisms = new FlowPane(8, 8);
            for (MechanismView mechanism : location.mechanisms()) {
                if (mechanism.locked()) {
                    Button button = new Button("Interagisci: " + mechanism.name());
                    button.setTooltip(new Tooltip(mechanism.description()));
                    button.setOnAction(e -> onInteract.accept(mechanism));
                    mechanisms.getChildren().add(button);
                } else {
                    mechanisms.getChildren().add(new Label(mechanism.name() + " (sbloccato)"));
                }
            }
            getChildren().add(mechanisms);
        }

        if (!location.items().isEmpty()) {
            getChildren().add(sectionTitle("Oggetti nel luogo"));
            TilePane tiles = new TilePane(10, 10);
            for (ItemView item : location.items()) {
                tiles.getChildren().add(new ItemCard(item, images.imageFor(ImageKind.ITEM, item.id()),
                        "Raccogli", onTake, onExamine));
            }
            getChildren().add(tiles);
        }

        getChildren().add(sectionTitle("Uscite"));
        FlowPane exits = new FlowPane(8, 8);
        for (ExitView exit : location.exits()) {
            Button button = new Button(exit.passable() ? exit.label() : exit.label() + " (bloccato)");
            button.setDisable(!exit.passable());
            button.setOnAction(e -> onExit.accept(exit));
            exits.getChildren().add(button);
        }
        getChildren().add(exits);
    }

    private HBox createHeader(LocationView location) {
        Label title = new Label(location.name());
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold");
        Label description = new Label(location.description());
        description.setWrapText(true);
        VBox texts = new VBox(6, title, description);

        // Dettagli visibili solo con certi attributi (es. Percezione): in corsivo sotto la descrizione.
        for (String detail : location.details()) {
            Label label = new Label(detail);
            label.setWrapText(true);
            label.setStyle("-fx-font-style: italic; -fx-text-fill: #5a4e2f");
            texts.getChildren().add(label);
        }

        HBox header = new HBox(16,
                IconFactory.create(location.name(), images.imageFor(ImageKind.LOCATION, location.id()), LOCATION_ICON_SIZE),
                texts);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-padding: 8 0 0 0");
        return label;
    }
}