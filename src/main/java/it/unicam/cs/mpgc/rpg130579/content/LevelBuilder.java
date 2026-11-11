package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;

public interface LevelBuilder {
    Level build(ItemCatalog catalog);
}
