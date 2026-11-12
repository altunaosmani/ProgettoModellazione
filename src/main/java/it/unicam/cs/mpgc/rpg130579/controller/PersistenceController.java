package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;

public interface PersistenceController {
    boolean hasSave();
    ActionResult save();
    ActionResult load();
    void newGame();
}
