package it.unicam.cs.mpgc.rpg130579.Content;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.DocumentItem;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.NPC;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Quest;
import it.unicam.cs.mpgc.rpg130579.Model.World.Exit;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

public class ForestLevelBuilder implements LevelBuilder {
    private static final String HOME = "casa";
    private static final String SQUARE = "piazza";

    @Override
    public Level build(ItemCatalog catalog) {
        Level level = new Level("livello1", "La Foresta dei Sussurri",
                "Il medaglione si è aperto: inizia la ricerca di Elara.",
                new Quest("quest1", "Segui le tracce di Elara",
                        "Scopri dove è andata tua madre."));

        addLocations(level);
        connectLocations(level);
        placeItems(level, catalog);
        placeNpcs(level);

        level.setStartLocation(HOME);
        return level;
    }

    private void addLocations(Level level) {
        level.addPlace(new Location(HOME, "Casa",
                "La tua casa a Tanusha. Il medaglione si è appena aperto."));
        level.addPlace(new Location(SQUARE, "Piazza del villaggio",
                "Il centro di Tanusha, dove gli abitanti si incontrano."));
    }

    private void connectLocations(Level level) {
        level.getPlaceById(HOME).addExit(new Exit(SQUARE, "Vai alla piazza", SQUARE));
        level.getPlaceById(SQUARE).addExit(new Exit(HOME, "Torna a casa", HOME));
    }

    private void placeItems(Level level, ItemCatalog catalog) {
        DocumentItem book = new DocumentItem("libro_madre", "Vecchio libro",
                "Un libro appartenuto a tua madre.",
                "Sul retro dell'immagine di Elara è scritto: \"Tanusha non è il posto da cui sono partita.\"");
        catalog.register(book);
        level.getPlaceById(HOME).addItem(book);
    }

    private void placeNpcs(Level level) {
        Location square = level.getPlaceById(SQUARE);
        square.addNPC(new NPC("orwen", "Orwen",
                "Un vecchio bibliotecario che conosceva Elara.",
                square, ForestDialogues.orwen()));
    }
}
