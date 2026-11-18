package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/** Barra dei menu superiore con le azioni di gestione partita (Nuova, Salva, Carica, Esci). */
public class AppMenuBar extends MenuBar {
    private final MenuItem saveItem = new MenuItem("Salva");

    public AppMenuBar(AppNavigator navigator) {
        MenuItem newGame = new MenuItem("Nuova partita");
        newGame.setOnAction(e -> navigator.requestNewGame());
        saveItem.setOnAction(e -> navigator.saveGame());
        MenuItem load = new MenuItem("Carica");
        load.setOnAction(e -> navigator.loadGame());
        MenuItem exit = new MenuItem("Esci");
        exit.setOnAction(e -> navigator.quit());
        getMenus().add(new Menu("Partita", null, newGame, saveItem, load, exit));
    }

    /** Si può salvare solo mentre si sta giocando. */
    public void setSaveEnabled(boolean enabled) {
        saveItem.setDisable(!enabled);
    }
}