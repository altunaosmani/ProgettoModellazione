package it.unicam.cs.mpgc.rpg130579.Content;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.DocumentItem;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.NPC;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Quest;
import it.unicam.cs.mpgc.rpg130579.Model.World.Exit;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

import java.util.List;

public class ForestLevelBuilder implements LevelBuilder {
    @Override
    public Level build(ItemCatalog catalog) {
        Level level = new Level("livello1", "La Foresta dei Sussurri",
                "Il medaglione si è aperto: inizia la ricerca di Elara.",
                new Quest("quest1", "Segui le tracce di Elara",
                        "Scopri dove è andata tua madre."));

        Location home = new Location("casa", "Casa", "La tua casa a Tanusha. Il medaglione si è appena aperto.");
        Location square = new Location("piazza", "Piazza del villaggio", "Il centro di Tanusha, dove gli abitanti si incontrano.");
        level.addPlace(home);
        level.addPlace(square);
        level.setStartLocation(home.getId());

        home.addExit(new Exit("piazza", "Vai alla piazza", square.getId()));
        square.addExit(new Exit("casa", "Torna a casa", home.getId()));

        DocumentItem book = new DocumentItem("libro_madre", "Vecchio libro",
                "Un libro appartenuto a tua madre.",
                "Sul retro dell'immagine di Elara è scritto: \"Tanusha non è il posto da cui sono partita.\"");
        catalog.register(book);
        home.addItem(book);

        Dialogue orwenDialogue = new Dialogue("Orwen", List.of(
                "Quel simbolo... lo conosco. Tua madre non era una semplice abitante di Tanusha.",
                "L'ultima volta che l'ho vista stava andando verso la Foresta dei Sussurri."));
        square.addNPC(new NPC("orwen", "Orwen", "Un vecchio bibliotecario che conosceva Elara.", square, orwenDialogue));

        return level;
    }
}
