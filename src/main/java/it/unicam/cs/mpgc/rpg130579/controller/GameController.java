package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LocationView;

import java.util.List;

/** Unico punto di accesso di una UI al gioco: esplorazione e oggetti. */
public interface GameController {
    LocationView currentLocation();

    List<ItemView> inventory();

    ActionResult move(String exitId);

    ActionResult takeItem(String itemId);

    boolean isGameOver();
}
