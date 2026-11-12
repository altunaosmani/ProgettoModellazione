package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LocationView;

import java.util.List;

public interface ExplorationController {
    LocationView currentLocation();
    List<ItemView> inventory();
    ActionResult move(String exitId);
    ActionResult takeItem(String itemId);
    ActionResult unlock(String mechanismId, List<String> answer);
}
