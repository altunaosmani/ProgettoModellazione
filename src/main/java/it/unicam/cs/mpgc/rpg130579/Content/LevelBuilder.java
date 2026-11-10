package it.unicam.cs.mpgc.rpg130579.Content;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;

public interface LevelBuilder {
    Level build(ItemCatalog catalog);
}
